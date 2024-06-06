package rf.senla.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rf.senla.domain.entity.Comment;
import rf.senla.domain.entity.Role;
import rf.senla.domain.entity.User;
import rf.senla.domain.exception.EntityContainedException;
import rf.senla.domain.exception.ErrorMessage;
import rf.senla.domain.exception.NoEntityException;
import rf.senla.domain.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final UserService userService;
    private final CommentRepository repository;

    @Override
    @Transactional
    public Comment create(Comment comment, UserDetails sender) {
        log.info("Сохранение комментария {}", comment);

        if (comment.getId() != null && repository.existsById(comment.getId())) {
            log.error("Не удалось сохранить комментарий {}", comment);
            throw new EntityContainedException(ErrorMessage.COMMENT_ALREADY_EXISTS.getMessage());
        }

        comment.setUser(userService.getByUsername(sender.getUsername()));
        comment.setCreatedAt(LocalDateTime.now());
        comment = repository.save(comment);
        log.info("Удалось сохранить комментарий {}", comment);
        return comment;
    }

    @Override
    @Transactional
    public Comment update(Comment comment, UserDetails user) {
        log.info("Обновление комментария {} пользователем {}", comment, user.getUsername());
        Comment entity = getById(comment.getId());
        checkSenderAndCurrentUser(user, entity.getUser());
        entity.setText(comment.getText());
        entity = repository.save(entity);
        log.info("Удалось обновить комментарий {} пользователю {}", entity, user.getUsername());
        return entity;
    }

    @Override
    @Transactional
    public void delete(Long id, UserDetails user) {
        log.info("Удаление комментария с ID {}", id);
        Comment comment = getById(id);
        try {
            checkSenderAndCurrentUser(user, comment.getUser());
        } catch (AccessDeniedException e) {
            User currentUser = userService.getByUsername(user.getUsername());
            if (!currentUser.getRole().equals(Role.ROLE_ADMIN)) {
                log.error("Текущий пользователь не является админом");
                throw e;
            }
        }

        repository.delete(comment);
        log.error("Удалось обновить комментарий {}", comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAll(Long advertisementId, Pageable pageable) {
        log.info("Получение списка комментариев по объявлению - {}, с пагинацией - {}",
                advertisementId, pageable);
        List<Comment> list = repository.findByAdvertisement_Id(advertisementId, pageable);
        log.info("Получен список из {} комментариев: {}", list.size(), list);
        return list;
    }

    /**
     * Получение комментария по ID
     * @param id ID комментария
     * @return комментарий
     */
    private Comment getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoEntityException(ErrorMessage.NO_COMMENT_FOUND.getMessage()));
    }

    /**
     * Служебный метод проверяет совпадение переданного пользователя и пользователя из объявления
     * @param currentUser переданный пользователь
     * @param sender пользователь объявления
     */
    private static void checkSenderAndCurrentUser(UserDetails currentUser, UserDetails sender) {
        String expectedUsername = currentUser.getUsername();
        if (!sender.getUsername().equals(expectedUsername)) {
            log.error("Переданный пользователь {} и пользователь объявления {} не совпали",
                    expectedUsername, sender);
            throw new AccessDeniedException(ErrorMessage.SENDER_MISMATCH.getMessage());
        }
    }
}

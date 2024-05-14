package rf.senla.advertisement.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rf.senla.advertisement.domain.entity.Comment;
import rf.senla.advertisement.domain.exception.AccessDeniedException;
import rf.senla.advertisement.domain.exception.ErrorMessage;
import rf.senla.advertisement.domain.repository.CommentRepository;
import rf.senla.advertisement.security.entity.Role;
import rf.senla.advertisement.security.entity.User;
import rf.senla.advertisement.security.utils.CurrentUserValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Comment save(Comment entity) {
        return commentRepository.save(entity);
    }

    @Transactional
    @Override
    public Comment update(Comment entity) {
        User user = entity.getUser();

        if (!CurrentUserValidator.isCurrentUser(user) && !user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new AccessDeniedException(ErrorMessage.USER_IS_NOT_ADMIN_OR_AUTHOR.getMessage());
        }

        return commentRepository.save(entity);
    }

    @Transactional
    @Override
    public void delete(Comment entity) {
        User user = entity.getUser();

        if (!CurrentUserValidator.isCurrentUser(user) && !user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new AccessDeniedException(ErrorMessage.USER_IS_NOT_ADMIN_OR_AUTHOR.getMessage());
        }

        commentRepository.delete(entity);
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }
}

package rf.senla.advertisement.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.domain.entity.Comment;
import rf.senla.advertisement.domain.exception.AccessDeniedException;
import rf.senla.advertisement.domain.exception.ErrorMessage;
import rf.senla.advertisement.domain.repository.CommentRepository;
import rf.senla.advertisement.security.entity.User;
import rf.senla.advertisement.security.utils.validator.UserPermissionsValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final CommentRepository repository;

    @Transactional
    @Override
    public Comment save(Comment entity) {
        return repository.save(entity);
    }

    @Transactional
    @Override
    public Comment update(Comment entity) {
        User user = entity.getUser();

        if (!UserPermissionsValidator.validate(user)) {
            throw new AccessDeniedException(ErrorMessage.USER_IS_NOT_ADMIN_OR_AUTHOR.getMessage());
        }

        return repository.save(entity);
    }

    @Transactional
    @Override
    public void delete(Comment entity) {
        User user = entity.getUser();

        if (!UserPermissionsValidator.validate(user)) {
            throw new AccessDeniedException(ErrorMessage.USER_IS_NOT_ADMIN_OR_AUTHOR.getMessage());
        }

        repository.delete(entity);
    }

    @Override
    public List<Comment> getAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<Comment> getAll(Advertisement advertisement) {
        return repository.findByAdvertisementOrderByCreatedAtDesc(advertisement);
    }
}

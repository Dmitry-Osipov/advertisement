package rf.senla.advertisement.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.domain.entity.Comment;
import rf.senla.advertisement.domain.exception.EntityContainedException;
import rf.senla.advertisement.domain.exception.ErrorMessage;
import rf.senla.advertisement.domain.exception.NoEntityException;
import rf.senla.advertisement.domain.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final CommentRepository repository;

    @Transactional
    @Override
    public Comment save(Comment entity) {
        if (entity.getId() != null && repository.existsById(entity.getId())) {
            throw new EntityContainedException(ErrorMessage.COMMENT_ALREADY_EXISTS.getMessage());
        }

        return repository.save(entity);
    }

    @Transactional
    @Override
    public Comment update(Comment entity) {
        if (!repository.existsById(entity.getId())) {
            throw new NoEntityException(ErrorMessage.NO_COMMENT_FOUND.getMessage());
        }

        return repository.save(entity);
    }

    @Transactional
    @Override
    public void delete(Comment entity) {
        if (!repository.existsById(entity.getId())) {
            throw new NoEntityException(ErrorMessage.NO_COMMENT_FOUND.getMessage());
        }

        repository.delete(entity);
    }

    @Override
    public List<Comment> getAll() {
        return repository.findAll(getPageable(0, 10)).getContent();
    }

    @Override
    public List<Comment> getAll(Advertisement advertisement, Integer page, Integer size) {
        return repository.findByAdvertisement(advertisement, getPageable(page, size));
    }

    /**
     * Служебный метод формирует пагинацию по времени.
     * @param page порядковый номер страницы
     * @param size размер страницы
     * @return пагинация
     */
    private Pageable getPageable(Integer page, Integer size) {
        if (page == null) {
            page = 0;
        }

        if (size == null) {
            size = 10;
        }

        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}

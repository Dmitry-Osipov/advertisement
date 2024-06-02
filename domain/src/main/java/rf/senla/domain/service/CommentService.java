package rf.senla.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rf.senla.domain.entity.Advertisement;
import rf.senla.domain.entity.Comment;
import rf.senla.domain.exception.EntityContainedException;
import rf.senla.domain.exception.ErrorMessage;
import rf.senla.domain.exception.NoEntityException;
import rf.senla.domain.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService implements ICommentService {
    private final CommentRepository repository;

    @Transactional
    @Override
    public Comment save(Comment entity) {
        log.info("Сохранение комментария {}", entity);

        if (entity.getId() != null && repository.existsById(entity.getId())) {
            log.error("Не удалось сохранить комментарий {}", entity);
            throw new EntityContainedException(ErrorMessage.COMMENT_ALREADY_EXISTS.getMessage());
        }

        Comment comment = repository.save(entity);
        log.info("Удалось сохранить комментарий {}", comment);
        return comment;
    }

    @Transactional
    @Override
    public Comment update(Comment entity) {
        log.info("Обновление комментария {}", entity);

        if (!repository.existsById(entity.getId())) {
            log.error("Не удалось обновить комментарий {}", entity);
            throw new NoEntityException(ErrorMessage.NO_COMMENT_FOUND.getMessage());
        }

        Comment comment = repository.save(entity);
        log.info("Удалось обновить комментарий {}", comment);
        return comment;
    }

    @Transactional
    @Override
    public void delete(Comment entity) {
        log.info("Удаление комментария {}", entity);

        if (!repository.existsById(entity.getId())) {
            log.error("Не удалось удалить комментарий {}", entity);
            throw new NoEntityException(ErrorMessage.NO_COMMENT_FOUND.getMessage());
        }

        repository.delete(entity);
        log.error("Удалось обновить комментарий {}", entity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getAll() {
        log.info("Получение списка комментариев");
        List<Comment> list = repository.findAll(getPageable(0, 10)).getContent();
        successfullyListLog(list);
        return list;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getAll(Advertisement advertisement, Integer page, Integer size) {
        log.info("Получение списка комментариев по объявлению - {}, с номером страницы - {}, с разером страницы - {}",
                advertisement, page, size);
        List<Comment> list = repository.findByAdvertisement(advertisement, getPageable(page, size));
        successfullyListLog(list);
        return list;
    }

    /**
     * Служебный метод формирует пагинацию по времени.
     * @param page порядковый номер страницы
     * @param size размер страницы
     * @return пагинация
     */
    private static Pageable getPageable(Integer page, Integer size) {
        log.info("Вызван метод формирования пагинации для номера страницы - {}, размера страницы - {}", page, size);

        if (page == null) {
            page = 0;
            log.info("Для номера страницы присвоено дефолтное значение - {}", page);
        }

        if (size == null) {
            size = 10;
            log.info("Для размера страницы присвоено дефолтное значение - {}", size);
        }

        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    /**
     * Служебный метод логирует данные списка
     * @param list список
     */
    private static void successfullyListLog(List<Comment> list) {
        log.info("Получен список из {} комментариев: {}", list.size(), list);
    }
}

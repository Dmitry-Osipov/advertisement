package rf.senla.advertisement.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rf.senla.advertisement.domain.entity.Message;
import rf.senla.advertisement.domain.exception.EntityContainedException;
import rf.senla.advertisement.domain.exception.ErrorMessage;
import rf.senla.advertisement.domain.exception.NoEntityException;
import rf.senla.advertisement.domain.repository.MessageRepository;
import rf.senla.advertisement.security.entity.User;

import java.util.List;

/**
 * Сервис работы с сообщениями.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService implements IMessageService {
    private final MessageRepository repository;

    @Transactional
    @Override
    public Message save(Message entity) {
        log.info("Сохранение сообщения {}", entity);

        if (entity.getId() != null && repository.existsById(entity.getId())) {
            log.error("Не удалось сохранить сообщение {}", entity);
            throw new EntityContainedException(ErrorMessage.MESSAGE_ALREADY_EXISTS.getMessage());
        }

        Message message = repository.save(entity);
        log.info("Сохранено сообщение {}", message);
        return message;
    }

    @Transactional
    @Override
    public Message update(Message entity) {
        log.info("Обновление сообщения {}", entity);

        if (!repository.existsById(entity.getId())) {
            log.error("Не удалось обновить сообщение {}", entity);
            throw new NoEntityException(ErrorMessage.NO_MESSAGE_FOUND.getMessage());
        }

        Message message = repository.save(entity);
        log.info("Удалось обновить сообщение {}", message);
        return message;
    }

    @Transactional
    @Override
    public void delete(Message entity) {
        log.info("Удаление сообщения {}", entity);

        if (!repository.existsById(entity.getId())) {
            log.error("Не удалось удалить сообщение {}", entity);
            throw new NoEntityException(ErrorMessage.NO_MESSAGE_FOUND.getMessage());
        }

        repository.delete(entity);
        log.info("Удалось удалить сообщение {}", entity);
    }

    @Override
    public List<Message> getAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Получение списка сообщений от/для пользователя {}", user);
        Pageable pageable = getPageable(0, 10);
        List<Message> list = repository.findAllByUserId(user.getId(), pageable);
        successfullyListLog(list);
        return list;
    }

    @Override
    public List<Message> getAll(User user, Integer page, Integer size) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Получение списка сообщений между пользователями {} и {}, с номером страницы {}, " +
                "с размером страницы {}", currentUser, user, page, size);
        List<Message> list =
                repository.findMessagesBetweenUsers(currentUser.getId(), user.getId(), getPageable(page, size));
        successfullyListLog(list);
        return list;
    }

    /**
     * Служебный метод формирует пагинацию по времени.
     * @param page порядковый номер страницы
     * @param size размер страницы
     * @return пагинация
     */
    private Pageable getPageable(Integer page, Integer size) {
        log.info("Вызван метод формирования пагинации для порядкового номера страницы - {}, размера страницы - {}",
                page, size);

        if (page == null) {
            page = 0;
            log.info("Для номера страницы присвоено дефолтное значение - {}", page);
        }

        if (size == null) {
            size = 10;
            log.info("Для размера страницы присвоено дефолтное значение - {}", size);
        }

        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sentAt"));
    }

    /**
     * Служебный метод логирует данные списка
     * @param list список
     */
    private static void successfullyListLog(List<Message> list) {
        log.info("Получен список из {} сообщений: {}", list.size(), list);
    }
}

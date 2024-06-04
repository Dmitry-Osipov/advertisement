package rf.senla.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rf.senla.domain.entity.Message;
import rf.senla.domain.exception.EntityContainedException;
import rf.senla.domain.exception.ErrorMessage;
import rf.senla.domain.exception.NoEntityException;
import rf.senla.domain.repository.MessageRepository;
import rf.senla.domain.entity.User;

import java.util.List;

/**
 * Сервис работы с сообщениями.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {
    private final MessageRepository repository;
    private final IUserService userService;

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

    @Transactional(readOnly = true)
    @Override
    public List<Message> getAll(UserDetails sender, String recipientName, Integer page, Integer size) {
        User user = (User) sender;
        User recipient = userService.getByUsername(recipientName);
        log.info("Получение списка сообщений между пользователями {} и {}, с номером страницы {}, " +
                "с размером страницы {}", user, recipient, page, size);
        List<Message> list =
                repository.findMessagesBetweenUsers(user.getId(), recipient.getId(), getPageable(page, size));
        successfullyListLog(list);
        return list;
    }

    // TODO: remove
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

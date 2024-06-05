package rf.senla.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rf.senla.domain.entity.Message;
import rf.senla.domain.exception.EntityContainedException;
import rf.senla.domain.exception.ErrorMessage;
import rf.senla.domain.exception.NoEntityException;
import rf.senla.domain.repository.MessageRepository;
import rf.senla.domain.entity.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис работы с сообщениями.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("java:S6809")
public class MessageService implements IMessageService {
    private final IUserService userService;
    private final MessageRepository repository;

    @Override
    @Transactional
    public Message create(Message entity, UserDetails sender) {
        log.info("Сохранение сообщения {}", entity);

        User user = userService.getByUsername(sender.getUsername());
        if (entity.getId() != null && repository.existsById(entity.getId())) {
            log.error("Не удалось сохранить сообщение {}", entity);
            throw new EntityContainedException(ErrorMessage.MESSAGE_ALREADY_EXISTS.getMessage());
        }

        entity.setRead(Boolean.FALSE);
        entity.setSender(user);
        entity.setSentAt(LocalDateTime.now());
        Message message = repository.save(entity);
        log.info("Сохранено сообщение {}", message);
        return message;
    }

    @Override
    @Transactional
    public Message update(Message entity, UserDetails sender) {
        log.info("Обновление сообщения {}", entity);
        Message message = getById(entity.getId());
        checkSenderAndCurrentUser(sender, message);
        message.setRead(entity.getRead() == null ? Boolean.FALSE : entity.getRead());
        message.setText(entity.getText());
        message = repository.save(message);
        log.info("Удалось обновить сообщение {}", message);
        return message;
    }

    @Override
    @Transactional
    public void delete(Message entity, UserDetails sender) {
        log.info("Удаление сообщения {}", entity);
        Message message = getById(entity.getId());
        checkSenderAndCurrentUser(sender, message);
        repository.delete(message);
        log.info("Удалось удалить сообщение {}", entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getAll(UserDetails sender, String recipientName, Pageable pageable) {
        User user = (User) sender;
        User recipient = userService.getByUsername(recipientName);
        log.info("Получение списка сообщений между пользователями {} и {}, с пагинацией {}", user, recipient, pageable);
        List<Message> list = repository.findMessagesBetweenUsers(user.getId(), recipient.getId(), pageable);
        log.info("Получен список из {} сообщений: {}", list.size(), list);
        return list;
    }

    /**
     * Метод проверяет совпадение текущего пользователя и отправителя сообщения
     * @param currentUser текущий пользователь
     * @param message сообщение
     * @throws AccessDeniedException если текущий пользователь не является отправителем
     */
    private static void checkSenderAndCurrentUser(UserDetails currentUser, Message message) {
        if (!message.getSender().getUsername().equals(currentUser.getUsername())) {
            log.error("Отправитель не является текущим пользователем");
            throw new AccessDeniedException(ErrorMessage.SENDER_MISMATCH.getMessage());
        }
    }

    /**
     * Получение сообщения по ID
     * @param id ID сообщения
     * @return сообщение
     * @throws NoEntityException если сообщение не было найдено
     */
    private Message getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoEntityException(ErrorMessage.NO_MESSAGE_FOUND.getMessage()));
    }
}

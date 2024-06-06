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
    private final AdvertisementService advertisementService;

    @Override
    @Transactional
    public Message create(Message message, UserDetails sender) {
        log.info("Сохранение сообщения {}", message);

        if (message.getId() != null && repository.existsById(message.getId())) {
            log.error("Не удалось сохранить сообщение {}", message);
            throw new EntityContainedException(ErrorMessage.MESSAGE_ALREADY_EXISTS.getMessage());
        }

        message.setSender(userService.getByUsername(sender.getUsername()));
        message.setRecipient(userService.getByUsername(message.getRecipient().getUsername()));
        message.setAdvertisement(advertisementService.getById(message.getAdvertisement().getId()));
        message.setSentAt(LocalDateTime.now());
        message.setRead(Boolean.FALSE);
        message = repository.save(message);
        log.info("Сохранено сообщение {}", message);
        return message;
    }

    @Override
    @Transactional
    public Message update(Message message, UserDetails sender) {
        log.info("Обновление сообщения {}", message);
        Message entity = getById(message.getId());
        checkSenderAndCurrentUser(sender, entity.getSender());
        entity.setRead(message.getRead() == null ? Boolean.FALSE : message.getRead());
        entity.setText(message.getText());
        entity = repository.save(entity);
        log.info("Удалось обновить сообщение {}", entity);
        return entity;
    }

    @Override
    @Transactional
    public void delete(Message message, UserDetails sender) {
        log.info("Удаление сообщения {}", message);
        Message entity = getById(message.getId());
        checkSenderAndCurrentUser(sender, entity.getSender());
        repository.delete(entity);
        log.info("Удалось удалить сообщение {}", message);
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
     * @param sender отправитель
     * @throws AccessDeniedException если текущий пользователь не является отправителем
     */
    private static void checkSenderAndCurrentUser(UserDetails currentUser, UserDetails sender) {
        if (!sender.getUsername().equals(currentUser.getUsername())) {
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

package rf.senla.domain.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import rf.senla.domain.entity.Message;

import java.util.List;

/**
 * Интерфейс для работы с сообщениями.
 */
public interface IMessageService {
    /**
     * Сохранение сообщения
     * @param message сообщение
     * @param sender данные пользователя
     * @return сохранённое сообщение
     */
    Message create(Message message, UserDetails sender);

    /** Обновление сообщения
     * @param message сообщение
     * @param sender данные пользователя
     * @return обновлённое сообщение
     */
    Message update(Message message, UserDetails sender);

    /** Удаление сообщения
     * @param message сообщение
     * @param sender данные пользователя
     */
    void delete(Message message, UserDetails sender);

    /**
     * Получить все сообщения от или к конкретному пользователю с пагинацией.
     * @param sender отправитель
     * @param recipientName логин получателя
     * @param pageable пагинация
     * @return список сообщений
     */
    List<Message> getAll(UserDetails sender, String recipientName, Pageable pageable);
}

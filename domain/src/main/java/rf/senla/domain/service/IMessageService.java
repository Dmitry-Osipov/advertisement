package rf.senla.domain.service;

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
     * @return сохранённое сообщение
     */
    Message save(Message message);

    /** Обновление сообщения
     * @param message сообщение
     * @return обновлённое сообщение
     */
    Message update(Message message);

    /** Удаление сообщения
     * @param message сообщение
     */
    void delete(Message message);

    /**
     * Получить все сообщения от или к конкретному пользователю с пагинацией.
     * @param sender отправитель
     * @param recipientName логин получателя
     * @param page порядковый номер страницы
     * @param size размер страницы
     * @return список сообщений
     */
    List<Message> getAll(UserDetails sender, String recipientName, Integer page, Integer size);
}

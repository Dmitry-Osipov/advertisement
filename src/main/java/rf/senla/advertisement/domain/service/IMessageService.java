package rf.senla.advertisement.domain.service;

import rf.senla.advertisement.domain.entity.Message;
import rf.senla.advertisement.security.entity.User;

import java.util.List;

/**
 * Интерфейс для работы с сообщениями.
 */
public interface IMessageService extends IService<Message> {
    /**
     * Получить все сообщения от или к конкретному пользователю.
     * @param user пользователь
     * @return список сообщений
     */
    List<Message> getAll(User user);
}

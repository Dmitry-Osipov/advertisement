package rf.senla.domain.service;

import rf.senla.domain.entity.Message;
import rf.senla.domain.entity.User;

import java.util.List;

/**
 * Интерфейс для работы с сообщениями.
 */
public interface IMessageService extends IService<Message> {
    /**
     * Получить все сообщения от или к конкретному пользователю с пагинацией.
     * @param user пользователь
     * @param page порядковый номер страницы
     * @param size размер страницы
     * @return список сообщений
     */
    List<Message> getAll(User user, Integer page, Integer size);
}

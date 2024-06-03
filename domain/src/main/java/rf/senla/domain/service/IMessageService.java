package rf.senla.domain.service;

import org.springframework.security.core.userdetails.UserDetails;
import rf.senla.domain.entity.Message;

import java.util.List;

/**
 * Интерфейс для работы с сообщениями.
 */
public interface IMessageService extends IService<Message> {
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

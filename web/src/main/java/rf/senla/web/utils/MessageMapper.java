package rf.senla.web.utils;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import rf.senla.web.dto.CreateMessageRequest;
import rf.senla.web.dto.DeleteByIdRequest;
import rf.senla.web.dto.MessageDto;
import rf.senla.web.dto.UpdateMessageRequest;
import rf.senla.domain.entity.Message;

import java.util.List;

/**
 * Маппер сообщений
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, AdvertisementMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MessageMapper {
    /**
     * Метод маппит сущность {@link Message} в DTO {@link MessageDto}
     * @param message сообщение
     * @return DTO сообщения
     */
    MessageDto toDto(Message message);

    /**
     * Метод маппит запрос на создание сообщения {@link CreateMessageRequest} в сообщение {@link Message}
     * @param request запрос на создание сообщения
     * @return сообщение
     */
    Message toEntity(CreateMessageRequest request);

    /**
     * Метод маппит запрос на обновление сообщения {@link UpdateMessageRequest} в сообщение {@link Message}
     * @param request запрос на обновление сообщения
     * @return сообщение
     */
    Message toEntity(UpdateMessageRequest request);

    /**
     * Метод маппит запрос на удаление по ID {@link DeleteByIdRequest} в сообщение {@link Message}
     * @param request запрос на удаление по ID
     * @return сообщение
     */
    Message toEntity(DeleteByIdRequest request);

    /**
     * Метод маппит список сообщений {@link Message} в список DTO {@link MessageDto}
     * @param messages список сообщений
     * @return список DTO
     */
    List<MessageDto> toDtos(List<Message> messages);
}

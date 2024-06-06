package rf.senla.web.utils;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import rf.senla.web.dto.CreateMessageRequest;
import rf.senla.web.dto.DeleteByIdRequest;
import rf.senla.web.dto.MessageDto;
import rf.senla.web.dto.UpdateMessageRequest;
import rf.senla.domain.entity.Message;

import java.util.List;

// TODO: javadoc
@Mapper(componentModel = "spring", uses = {UserMapper.class, AdvertisementMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MessageMapper {
    MessageDto toDto(Message message);
    Message toEntity(CreateMessageRequest request);
    Message toEntity(UpdateMessageRequest request);
    Message toEntity(DeleteByIdRequest request);
    List<MessageDto> toDtos(List<Message> messages);
}

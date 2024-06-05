package rf.senla.web.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rf.senla.domain.dto.CommentDto;
import rf.senla.domain.dto.MessageDto;
import rf.senla.domain.entity.Comment;
import rf.senla.domain.entity.Message;
import rf.senla.domain.service.IAdvertisementService;
import rf.senla.domain.service.IUserService;

import java.util.List;

/**
 * Утилитарный класс для конвертации между объектами пакета entity и их dto.
 */
@Component
@RequiredArgsConstructor
public final class DtoConverter {
    private final IUserService userService;
    private final IAdvertisementService advertisementService;

    /**
     * Преобразует объект {@link Comment} в соответствующий ему DTO.
     * @param comment Комментарий, который требуется преобразовать.
     * @return {@link CommentDto}, представляющий переданный комментарий.
     */
    public CommentDto getDtoFromComment(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .advertisementId(comment.getAdvertisement().getId())
                .userName(comment.getUser().getUsername())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    /**
     * Преобразует DTO {@link CommentDto} в объект {@link Comment}.
     * @param dto DTO, который требуется преобразовать.
     * @return Объект {@link Comment}, представляющий переданный DTO.
     */
    public Comment getCommentFromDto(CommentDto dto) {
        return Comment.builder()
                .id(dto.getId())
                .advertisement(advertisementService.getById(dto.getAdvertisementId()))
                .user(userService.getByUsername(dto.getUserName()))
                .text(dto.getText())
                .createdAt(dto.getCreatedAt())
                .build();
    }

    /**
     * Преобразует список объектов {@link Comment} в список соответствующих им DTO.
     * @param comments Список комментариев, которые требуется преобразовать.
     * @return Список {@link CommentDto}, представляющих переданные комментарии.
     */
    public List<CommentDto> getListCommentDto(List<Comment> comments) {
        return comments.stream()
                .map(this::getDtoFromComment)
                .toList();
    }

    /**
     * Преобразует объект {@link Message} в объект {@link MessageDto}.
     * @param message объект {@link Message}, который нужно преобразовать
     * @return объект {@link MessageDto}, соответствующий переданному {@link Message}
     */
    public MessageDto getDtoFromMessage(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .advertisementId(message.getAdvertisement().getId())
                .senderName(message.getSender().getUsername())
                .recipientName(message.getRecipient().getUsername())
                .text(message.getText())
                .sentAt(message.getSentAt())
                .read(message.getRead())
                .build();
    }

    /**
     * Преобразует объект {@link MessageDto} в объект {@link Message}.
     * @param dto объект {@link MessageDto}, который нужно преобразовать
     * @return объект {@link Message}, соответствующий переданному {@link MessageDto}
     */
    public Message getMessageFromDto(MessageDto dto) {
        return Message.builder()
                .id(dto.getId())
                .advertisement(advertisementService.getById(dto.getAdvertisementId()))
                .sender(userService.getByUsername(dto.getSenderName()))
                .recipient(userService.getByUsername(dto.getRecipientName()))
                .text(dto.getText())
                .sentAt(dto.getSentAt())
                .read(dto.getRead())
                .build();
    }

    /**
     * Преобразует список объектов {@link Message} в список объектов {@link MessageDto}.
     * @param messages список объектов {@link Message}, которые нужно преобразовать
     * @return список объектов {@link MessageDto}, соответствующих переданным {@link Message}
     */
    public List<MessageDto> getListMessageDto(List<Message> messages) {
        return messages.stream()
                .map(this::getDtoFromMessage)
                .toList();
    }
}

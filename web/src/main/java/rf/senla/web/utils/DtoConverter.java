package rf.senla.web.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rf.senla.domain.dto.AdvertisementDto;
import rf.senla.domain.dto.CommentDto;
import rf.senla.domain.dto.MessageDto;
import rf.senla.domain.dto.UserDto;
import rf.senla.domain.entity.Advertisement;
import rf.senla.domain.entity.AdvertisementStatus;
import rf.senla.domain.entity.Comment;
import rf.senla.domain.entity.Message;
import rf.senla.domain.entity.User;
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
     * Преобразует объект типа {@link Advertisement} в объект типа UserDto.
     * @param advertisement Объект типа {@link Advertisement}, который нужно преобразовать.
     * @return Объект типа {@link AdvertisementDto}, содержащий данные из переданного объекта {@link Advertisement}.
     */
    public AdvertisementDto getDtoFromAdvertisement(Advertisement advertisement) {
        return AdvertisementDto.builder()
                .id(advertisement.getId())
                .userName(advertisement.getUser().getUsername())
                .price(advertisement.getPrice())
                .headline(advertisement.getHeadline())
                .description(advertisement.getDescription())
                .status(String.valueOf(advertisement.getStatus()))
                .build();
    }

    /**
     * Преобразует объект типа {@link AdvertisementDto} в объект типа {@link Advertisement}.
     * @param dto Объект типа {@link AdvertisementDto}, который нужно преобразовать.
     * @return Объект типа {@link Advertisement}, содержащий данные из переданного объекта {@link AdvertisementDto}.
     */
    public Advertisement getAdvertisementFromDto(AdvertisementDto dto) {
        return Advertisement.builder()
                .id(dto.getId())
                .user(userService.getByUsername(dto.getUserName()))
                .price(dto.getPrice())
                .headline(dto.getHeadline())
                .description(dto.getDescription())
                .status(AdvertisementStatus.valueOf(dto.getStatus()))
                .build();
    }

    /**
     * Преобразует список объектов объявлений в список объектов DTO.
     * @param advertisements список объектов объявлений
     * @return список объектов DTO
     */
    public List<AdvertisementDto> getListAdvertisementDto(List<Advertisement> advertisements) {
        return advertisements.stream()
                .map(this::getDtoFromAdvertisement)
                .toList();
    }

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

    /**
     * Преобразует объект типа {@link User} в объект типа {@link UserDto}.
     * @param user Объект типа {@link User}, который нужно преобразовать.
     * @return Объект типа {@link UserDto}, содержащий данные из переданного объекта {@link User}.
     */
    public UserDto getDtoFromUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .rating(user.getRating())
                .email(user.getEmail())
                .boosted(user.getBoosted())
                .role(user.getRole())
                .build();
    }

    /**
     * Преобразует объект типа {@link UserDto} в объект типа {@link User}.
     * @param dto Объект типа {@link UserDto}, который нужно преобразовать.
     * @return Объект типа {@link User}, содержащий данные из переданного объекта {@link UserDto}.
     */
    public User getUserFromDto(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .rating(dto.getRating())
                .email(dto.getEmail())
                .boosted(dto.getBoosted())
                .role(dto.getRole())
                .build();
    }

    /**
     * Преобразует список объектов пользователя в список объектов DTO.
     * @param users список объектов пользователя
     * @return список объектов DTO
     */
    public List<UserDto> getListDto(List<User> users) {
        return users.stream()
                .map(this::getDtoFromUser)
                .toList();
    }
}

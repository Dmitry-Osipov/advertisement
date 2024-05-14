package rf.senla.advertisement.domain.utils;

import rf.senla.advertisement.domain.dto.AdvertisementDto;
import rf.senla.advertisement.domain.dto.CommentDto;
import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.domain.entity.AdvertisementStatus;
import rf.senla.advertisement.domain.entity.Comment;

import java.util.List;

/**
 * Утилитарный класс для конвертации между объектами пакета entity и их dto.
 */
public final class DtoConverter {
    private DtoConverter() {}

    /**
     * Преобразует объект типа {@link Advertisement} в объект типа UserDto.
     * @param advertisement Объект типа {@link Advertisement}, который нужно преобразовать.
     * @return Объект типа {@link AdvertisementDto}, содержащий данные из переданного объекта {@link Advertisement}.
     */
    public static AdvertisementDto getDtoFromAdvertisement(Advertisement advertisement) {
        return AdvertisementDto.builder()
                .id(advertisement.getId())
                .user(rf.senla.advertisement.security.utils.DtoConverter.getDtoFromUser(advertisement.getUser()))
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
    public static Advertisement getAdvertisementFromDto(AdvertisementDto dto) {
        return Advertisement.builder()
                .id(dto.getId())
                .user(rf.senla.advertisement.security.utils.DtoConverter.getUserFromDto(dto.getUser()))
                .price(dto.getPrice())
                .headline(dto.getHeadline())
                .description(dto.getDescription())
                .status(AdvertisementStatus.valueOf(dto.getStatus()))
                .build();
    }

    /**
     * Преобразует список объектов объявлений в список объектов DTO.
     * @param users список объектов объявлений
     * @return список объектов DTO
     */
    public static List<AdvertisementDto> getListAdvertisementDto(List<Advertisement> users) {
        return users.stream()
                .map(DtoConverter::getDtoFromAdvertisement)
                .toList();
    }

    /**
     * Преобразует список объектов DTO в список объектов объявлений.
     * @param dtos список объектов DTO
     * @return список объектов объявлений
     */
    public static List<Advertisement> getListAdvertisement(List<AdvertisementDto> dtos) {
        return dtos.stream()
                .map(DtoConverter::getAdvertisementFromDto)
                .toList();
    }

    // TODO: дока
    public static CommentDto getDtoFromComment(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .advertisement(DtoConverter.getDtoFromAdvertisement(comment.getAdvertisement()))
                .user(rf.senla.advertisement.security.utils.DtoConverter.getDtoFromUser(comment.getUser()))
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static Comment getCommentFromDto(CommentDto dto) {
        return Comment.builder()
                .id(dto.getId())
                .advertisement(DtoConverter.getAdvertisementFromDto(dto.getAdvertisement()))
                .user(rf.senla.advertisement.security.utils.DtoConverter.getUserFromDto(dto.getUser()))
                .text(dto.getText())
                .createdAt(dto.getCreatedAt())
                .build();
    }

    public static List<CommentDto> getListCommentDto(List<Comment> comments) {
        return comments.stream()
                .map(DtoConverter::getDtoFromComment)
                .toList();
    }

    public static List<Comment> getListComment(List<CommentDto> dtos) {
        return dtos.stream()
                .map(DtoConverter::getCommentFromDto)
                .toList();
    }
}

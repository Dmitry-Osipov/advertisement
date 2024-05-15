package rf.senla.advertisement.domain.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rf.senla.advertisement.domain.dto.AdvertisementDto;
import rf.senla.advertisement.domain.dto.CommentDto;
import rf.senla.advertisement.domain.entity.Advertisement;
import rf.senla.advertisement.domain.entity.AdvertisementStatus;
import rf.senla.advertisement.domain.entity.Comment;
import rf.senla.advertisement.domain.service.AdvertisementService;
import rf.senla.advertisement.security.service.IUserService;

import java.util.List;

/**
 * Утилитарный класс для конвертации между объектами пакета entity и их dto.
 */
@Component
@RequiredArgsConstructor
public final class DtoConverter {
    private final IUserService userService;
    private final AdvertisementService advertisementService;

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
     * @param users список объектов объявлений
     * @return список объектов DTO
     */
    public List<AdvertisementDto> getListAdvertisementDto(List<Advertisement> users) {
        return users.stream()
                .map(this::getDtoFromAdvertisement)
                .toList();
    }

    /**
     * Преобразует список объектов DTO в список объектов объявлений.
     * @param dtos список объектов DTO
     * @return список объектов объявлений
     */
    public List<Advertisement> getListAdvertisement(List<AdvertisementDto> dtos) {
        return dtos.stream()
                .map(this::getAdvertisementFromDto)
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
     * Преобразует список {@link CommentDto} в список объектов {@link Comment}.
     * @param dtos Список DTO, которые требуется преобразовать.
     * @return Список объектов {@link Comment}, представляющих переданные DTO.
     */
    public List<Comment> getListComment(List<CommentDto> dtos) {
        return dtos.stream()
                .map(this::getCommentFromDto)
                .toList();
    }
}

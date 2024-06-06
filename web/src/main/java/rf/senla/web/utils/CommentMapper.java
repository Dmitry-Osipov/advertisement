package rf.senla.web.utils;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import rf.senla.domain.entity.Comment;
import rf.senla.web.dto.CommentDto;
import rf.senla.web.dto.CreateCommentRequest;
import rf.senla.web.dto.UpdateCommentRequest;

import java.util.List;

/**
 * Маппер комментариев
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, AdvertisementMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {
    /**
     * Метод маппит сущность {@link Comment} в DTO {@link CommentDto}
     * @param comment комментарий
     * @return DTO комментария
     */
    CommentDto toDto(Comment comment);

    /**
     * Метод маппит DTO {@link CommentDto} в сущность {@link Comment}
     * @param dto DTO комментария
     * @return комментарий
     */
    Comment toEntity(CommentDto dto);

    /**
     * Метод маппит запрос на создание комментария {@link CreateCommentRequest} в сущность {@link Comment}
     * @param request запрос на создание комментария
     * @return комментарий
     */
    Comment toEntity(CreateCommentRequest request);

    /**
     * Метод маппит запрос на обновление комментария {@link UpdateCommentRequest} в сущность {@link Comment}
     * @param request запрос на обновление комментария
     * @return комментарий
     */
    Comment toEntity(UpdateCommentRequest request);

    /**
     * Метод маппит список комментариев {@link Comment} в список DTO {@link CommentDto}
     * @param comments список комментариев
     * @return список DTO
     */
    List<CommentDto> toDtos(List<Comment> comments);
}

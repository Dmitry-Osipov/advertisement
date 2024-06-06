package rf.senla.web.utils;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import rf.senla.domain.entity.Comment;
import rf.senla.web.dto.CommentDto;
import rf.senla.web.dto.CreateCommentRequest;
import rf.senla.web.dto.UpdateCommentRequest;

import java.util.List;

// TODO: javadoc
@Mapper(componentModel = "spring", uses = {UserMapper.class, AdvertisementMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {
    CommentDto toDto(Comment comment);
    Comment toEntity(CommentDto commentDto);
    Comment toEntity(CreateCommentRequest request);
    Comment toEntity(UpdateCommentRequest request);
    List<CommentDto> toDtos(List<Comment> comments);
}

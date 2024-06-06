package rf.senla.web.utils;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import rf.senla.web.dto.UpdateUserRequest;
import rf.senla.web.dto.UserDto;
import rf.senla.domain.entity.User;

import java.util.List;

// TODO: javadoc
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto dto);
    User toEntity(UpdateUserRequest request);
    List<UserDto> toDtos(List<User> users);
}

package rf.senla.web.utils;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import rf.senla.web.dto.SignInRequest;
import rf.senla.web.dto.SignUpRequest;
import rf.senla.web.dto.UpdateUserRequest;
import rf.senla.web.dto.UserDto;
import rf.senla.domain.entity.User;

import java.util.List;

/**
 * Маппер пользователей
 */
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    /**
     * Метод маппит сущность {@link User} в DTO {@link UserDto}
     * @param user пользователь
     * @return DTO пользователя
     */
    UserDto toDto(User user);

    /**
     * Метод маппит DTO {@link UserDto} в сущность {@link User}
     * @param dto DTO пользователя
     * @return пользователь
     */
    User toEntity(UserDto dto);

    /**
     * Метод маппит запрос на обновление пользователя {@link UpdateUserRequest} в сущность {@link User}
     * @param request запрос на обновление пользователя
     * @return пользователь
     */
    User toEntity(UpdateUserRequest request);

    /**
     * Метод маппит запрос на регистрацию {@link SignUpRequest} в сущность {@link User}
     * @param request запрос на регистрацию пользователя
     * @return пользователь
     */
    User toEntity(SignUpRequest request);

    /**
     * Метод маппит запрос на авторизацию {@link SignInRequest} в сущность {@link User}
     * @param request запрос на авторизацию пользователя
     * @return пользователь
     */
    User toEntity(SignInRequest request);

    /**
     * Метод маппит список сущностей {@link User} в список DTO {@link UserDto}
     * @param users список пользователей
     * @return список DTO
     */
    List<UserDto> toDtos(List<User> users);
}

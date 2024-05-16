package rf.senla.advertisement.security.utils;

import lombok.experimental.UtilityClass;
import rf.senla.advertisement.security.dto.UserDto;
import rf.senla.advertisement.security.entity.User;

import java.util.List;

/**
 * Утилитарный класс для конвертации между объектами {@link User} и {@link UserDto}.
 */
@UtilityClass
public final class DtoConverter {
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
                .map(DtoConverter::getDtoFromUser)
                .toList();
    }

}

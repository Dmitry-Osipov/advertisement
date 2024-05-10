package rf.senla.advertisement.security.utils;

import rf.senla.advertisement.security.dto.UserDto;
import rf.senla.advertisement.security.entity.User;

import java.util.List;

/**
 * Утилитарный класс для конвертации между объектами User и UserDto.
 */
public final class DtoConverter {
    private DtoConverter() {}

    /**
     * Преобразует объект типа User в объект типа UserDto.
     * @param user Объект типа User, который нужно преобразовать.
     * @return Объект типа UserDto, содержащий данные из переданного объекта User.
     */
    public static UserDto getDtoFromUser(User user) {
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
     * Преобразует объект типа UserDto в объект типа User.
     * @param dto Объект типа UserDto, который нужно преобразовать.
     * @return Объект типа User, содержащий данные из переданного объекта UserDto.
     */
    public static User getUserFromDto(UserDto dto) {
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
    public static List<UserDto> getListDto(List<User> users) {
        return users.stream()
                .map(DtoConverter::getDtoFromUser)
                .toList();
    }

    /**
     * Преобразует список объектов DTO в список объектов пользователя.
     * @param dtos список объектов DTO
     * @return список объектов пользователя
     */
    public static List<User> getListUser(List<UserDto> dtos) {
        return dtos.stream()
                .map(DtoConverter::getUserFromDto)
                .toList();
    }
}

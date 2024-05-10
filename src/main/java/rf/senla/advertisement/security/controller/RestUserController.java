package rf.senla.advertisement.security.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rf.senla.advertisement.security.dto.ChangePasswordRequest;
import rf.senla.advertisement.security.dto.UserDto;
import rf.senla.advertisement.security.entity.User;
import rf.senla.advertisement.security.service.UserService;
import rf.senla.advertisement.security.utils.DtoConverter;

import java.util.List;

/**
 * Контроллер для обработки запросов пользователей через REST API.
 */
@RestController
@RequestMapping("${spring.data.rest.base-path}/users")
@Validated
@RequiredArgsConstructor
@Tag(name = "Работа с пользователями")
public class RestUserController {
    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(DtoConverter.getDtoFromUser(userService.getByUsername(username)));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(DtoConverter.getListDto(userService.getAll()));
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserDto dto) {
        User user = DtoConverter.getUserFromDto(dto);
        return ResponseEntity.ok(DtoConverter.getDtoFromUser(userService.update(user)));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestBody @Valid UserDto dto) {
        userService.delete(DtoConverter.getUserFromDto(dto));
        return ResponseEntity.ok("Deleted user with username: " + dto.getUsername());
    }

    /**
     * Метод обновления пароля у пользователя
     * @param request запрос пользователя, с обновлением пароля
     * @return обновлённый пользователь
     */
    @PutMapping("/password")
    public ResponseEntity<UserDto> updatePassword(@RequestBody @Valid ChangePasswordRequest request) {
        return ResponseEntity.ok(DtoConverter.getDtoFromUser(userService.updatePassword(
                request.getUsername(), request.getOldPassword(), request.getNewPassword())));
    }

    /**
     * Метод для установки роли администратора для пользователя.
     * @param username логин пользователя
     * @return строку с сообщением об успешной установке роли администратора
     */
    @GetMapping("/admin/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public String setRoleAdmin(@PathVariable String username) {
        userService.setAdminRole(username);
        return "The admin role is set to " + username;
    }
}

package rf.senla.advertisement.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rf.senla.advertisement.security.dto.ChangePasswordRequest;
import rf.senla.advertisement.security.dto.UserDto;
import rf.senla.advertisement.security.entity.User;
import rf.senla.advertisement.security.service.IUserService;
import rf.senla.advertisement.security.utils.DtoConverter;

import java.util.List;

/**
 * Контроллер для обработки запросов пользователей через REST API.
 */
@RestController
@RequestMapping("${spring.data.rest.base-path}/users")
@Validated
@RequiredArgsConstructor
@Tag(name = "Пользователи")
@Slf4j
public class RestUserController {
    private final IUserService service;

    /**
     * Получить пользователя по его имени пользователя (логину).
     * @param username имя пользователя
     * @return ответ с пользователем
     */
    @GetMapping("/{username}")
    @Operation(summary = "Получить пользователя по его имени пользователя (логину)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = @ExampleObject(value = "{\"id\": 1,\"username\": \"John Doe\"," +
                                    "\"password\": \"my_1secret1_password\",\"phoneNumber\": \"+7(777)777-77-77\"," +
                                    "\"rating\": 100,\"email\": \"jondoe@gmail.com\",\"boosted\": false," +
                                    "\"role\": \"ROLE_USER\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<UserDto> getUserByUsername(
            @Parameter(description = "Имя пользователя", example = "John Doe", required = true, in = ParameterIn.PATH)
            @PathVariable String username) {
        log.info("Получение пользователя по имени {}", username);
        User user = service.getByUsername(username);
        log.info("Успешно получен пользователь {}", user);
        return ResponseEntity.ok(DtoConverter.getDtoFromUser(user));
    }

    /**
     * Получить 10 топовых пользователей.
     * @return ответ со списком пользователей
     */
    @GetMapping
    @Operation(summary = "Получить 10 топовых пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = @ExampleObject(value = "[ {\"id\": 1,\"username\": \"John Doe\"," +
                                    "\"password\": \"my_1secret1_password\",\"phoneNumber\": \"+7(777)777-77-77\"," +
                                    "\"rating\": 100,\"email\": \"johndoe@gmail.com\",\"boosted\": true," +
                                    "\"role\": \"ROLE_USER\"}, {\"id\": 2,\"username\": \"Jane Smith\"," +
                                    "\"password\": \"another_secret_password\",\"phoneNumber\": \"+7(888)888-88-88\"," +
                                    "\"rating\": 200,\"email\": \"janesmith@gmail.com\",\"boosted\": false," +
                                    "\"role\": \"ROLE_ADMIN\"} ]"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Получение списка 10 топовых пользователей");
        List<UserDto> list = DtoConverter.getListDto(service.getAll());
        successfullyListLog(list);
        return ResponseEntity.ok(list);
    }

    /**
     * Получить список пользователей с пагинацией.
     * @param page Порядковый номер страницы.
     * @param size Размер страницы.
     * @return ответ со списком пользователей
     */
    @GetMapping("/search")
    @Operation(summary = "Получить список пользователей с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = @ExampleObject(value = "[ {\"id\": 1,\"username\": \"John Doe\"," +
                                    "\"password\": \"my_1secret1_password\",\"phoneNumber\": \"+7(777)777-77-77\"," +
                                    "\"rating\": 100,\"email\": \"johndoe@gmail.com\",\"boosted\": false," +
                                    "\"role\": \"ROLE_USER\"}, {\"id\": 2,\"username\": \"Jane Smith\"," +
                                    "\"password\": \"another_secret_password\",\"phoneNumber\": \"+7(888)888-88-88\"," +
                                    "\"rating\": 200,\"email\": \"janesmith@gmail.com\",\"boosted\": true," +
                                    "\"role\": \"ROLE_ADMIN\"} ]"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<List<UserDto>> getUsersBySearch(
            @Parameter(description = "Номер страницы", example = "0", in = ParameterIn.QUERY)
            @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "Размер страницы", example = "1", in = ParameterIn.QUERY)
            @RequestParam(value = "size", required = false) Integer size) {
        log.info("Получение пользователей с номером страницы {} и размером страницы {}", page, size);
        List<UserDto> list = DtoConverter.getListDto(service.getAll(page, size));
        successfullyListLog(list);
        return ResponseEntity.ok(list);
    }

    /**
     * Обновить информацию о пользователе.
     * @param dto данные пользователя
     * @return ответ с обновленным пользователем
     */
    @PutMapping
    @PreAuthorize("#dto.username == authentication.principal.username")
    @Operation(summary = "Обновить информацию о пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = @ExampleObject(value = "{\"id\": 1,\"username\": \"John Doe\"," +
                                    "\"password\": \"my_1secret1_password\",\"phoneNumber\": \"+7(777)777-77-77\"," +
                                    "\"rating\": 300,\"email\": \"jondoe@gmail.com\",\"boosted\": false," +
                                    "\"role\": \"ROLE_USER\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "Данные пользователя", required = true,
                    content = @Content(schema = @Schema(implementation = UserDto.class)))
            @RequestBody @Valid UserDto dto) {
        log.info("Обновление пользователя {}", dto);
        UserDto user = DtoConverter.getDtoFromUser(service.update(DtoConverter.getUserFromDto(dto)));
        log.info("Успешно обновлён пользователь {}", user);
        return ResponseEntity.ok(user);
    }

    /**
     * Удалить пользователя.
     * @param dto данные пользователя
     * @return ответ об успешном удалении
     */
    @DeleteMapping
    @PreAuthorize("#dto.username == authentication.principal.username or hasRole('ADMIN')")
    @Operation(summary = "Удалить пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "Данные пользователя", required = true,
                    content = @Content(schema = @Schema(implementation = UserDto.class)))
            @RequestBody @Valid UserDto dto) {
        log.info("Удаление пользователя {}", dto);
        service.delete(DtoConverter.getUserFromDto(dto));
        log.info("Успешно удалён пользователь {}", dto);
        return ResponseEntity.ok("Deleted user with username: " + dto.getUsername());
    }

    /**
     * Метод обновления пароля у пользователя
     * @param request запрос пользователя, с обновлением пароля
     * @return обновлённый пользователь
     */
    @PutMapping("/password")
    @PreAuthorize("#request.username == authentication.principal.username")
    @Operation(summary = "Метод обновления пароля у пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = @ExampleObject(value = "{\"id\": 1,\"username\": \"John Doe\"," +
                                    "\"password\": \"my_1secret1_password\",\"phoneNumber\": \"+7(777)777-77-77\"," +
                                    "\"rating\": 100,\"email\": \"jondoe@gmail.com\",\"boosted\": false," +
                                    "\"role\": \"ROLE_USER\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<UserDto> updatePassword(
            @Parameter(description = "Данные смены пароля", required = true,
                    content = @Content(schema = @Schema(implementation = ChangePasswordRequest.class)))
            @RequestBody @Valid ChangePasswordRequest request) {
        log.info("Обновление пароля пользователя {}", request.getUsername());
        UserDto user = DtoConverter.getDtoFromUser(service.updatePassword(
                request.getUsername(), request.getOldPassword(), request.getNewPassword()));
        log.info("Успешно произошло обновление пароля пользователя {}", user);
        return ResponseEntity.ok(user);
    }

    /**
     * Метод для установки роли администратора для пользователя.
     * @param username логин пользователя
     * @return строку с сообщением об успешной установке роли администратора
     */
    @PutMapping("${spring.data.rest.admin-path}/role-admin/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Метод для установки роли администратора для пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<String> setRoleAdmin(
            @Parameter(description = "Имя пользователя", example = "John Doe", required = true, in = ParameterIn.PATH)
            @PathVariable String username) {
        log.info("Установка роли админа пользователю {}", username);
        service.setAdminRole(username);
        log.info("Удалось успешно установить роль админа пользователю {}", username);
        return ResponseEntity.ok("The admin role is set to " + username);
    }

    /**
     * Метод для продвижения пользователя.
     * @param username логин пользователя
     * @return строка с сообщением об успешном продвижении
     */
    @PutMapping("${spring.data.rest.admin-path}/boosted/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Метод для продвижения пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<String> setBoosted(
            @Parameter(description = "Имя пользователя", example = "John Doe", required = true, in = ParameterIn.PATH)
            @PathVariable String username) {
        log.info("Установка продвижения пользователю {}", username);
        service.setBoosted(username);
        log.info("Удалось успешно установить продвижение пользователю {}", username);
        return ResponseEntity.ok("User " + username + " has received a boost");
    }

    /**
     * Служебный метод логирует данные списка
     * @param list список
     */
    private static void successfullyListLog(List<UserDto> list) {
        log.info("Получен список из {} пользователей: {}", list.size(), list);
    }
}

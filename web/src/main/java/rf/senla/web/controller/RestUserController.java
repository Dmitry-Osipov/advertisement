package rf.senla.web.controller;

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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rf.senla.web.dto.ChangePasswordRequest;
import rf.senla.web.dto.DeleteByUsernameRequest;
import rf.senla.web.dto.UpdateUserRequest;
import rf.senla.web.dto.UserDto;
import rf.senla.domain.service.IUserService;
import rf.senla.web.utils.UserMapper;

import java.util.List;

// TODO: swagger docs update description for user
/**
 * Контроллер для обработки запросов пользователей через REST API.
 */
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Пользователи")
@RequestMapping("${spring.data.rest.base-path}/users")
public class RestUserController {
    private final IUserService service;
    private final UserMapper mapper;

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
    public ResponseEntity<UserDto> getByUsername(
            @Parameter(description = "Имя пользователя", example = "John Doe", required = true, in = ParameterIn.PATH)
            @PathVariable @NotBlank @Size(min = 5, max = 50) String username) {
        return ResponseEntity.ok(mapper.toDto(service.getByUsername(username)));
    }

    /**
     * Получить список пользователей с пагинацией.
     * @param pageable пагинация
     * @return ответ со списком пользователей
     */
    @GetMapping
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
    public ResponseEntity<List<UserDto>> getAll(
            @PageableDefault(sort = {"boosted", "rating"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(mapper.toDtos(service.getAll(pageable)));
    }

    /**
     * Обновить информацию о пользователе.
     * @param request данные пользователя
     * @return ответ с обновленным пользователем
     */
    @PutMapping
    @Operation(summary = "Обновить информацию о пользователе")
    @PreAuthorize("#request.id == authentication.principal.id or hasRole('ADMIN')")
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
    public ResponseEntity<UserDto> update(
            @Parameter(description = "Данные пользователя", required = true,
                    content = @Content(schema = @Schema(implementation = UpdateUserRequest.class)))
            @RequestBody @Valid UpdateUserRequest request) {
        return ResponseEntity.ok(mapper.toDto(service.update(mapper.toEntity(request))));
    }

    /**
     * Удалить пользователя.
     * @param request запрос с именем пользователя
     * @return ответ об успешном удалении
     */
    @DeleteMapping
    @Operation(summary = "Удалить пользователя")
    @PreAuthorize("#request.username == authentication.principal.username or hasRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<String> delete(
            @Parameter(description = "Данные пользователя", required = true,
                    content = @Content(schema = @Schema(implementation = UserDto.class)))
            @RequestBody @Valid DeleteByUsernameRequest request) {
        service.deleteByUsername(request.getUsername());
        return ResponseEntity.ok("Deleted user with username: " + request.getUsername());
    }

    /**
     * Метод обновления пароля у пользователя
     * @param request запрос пользователя, с обновлением пароля
     * @return обновлённый пользователь
     */
    @PutMapping("/password")
    @Operation(summary = "Метод обновления пароля у пользователя")
    @PreAuthorize("#request.username == authentication.principal.username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<String> updatePassword(
            @Parameter(description = "Данные смены пароля", required = true,
                    content = @Content(schema = @Schema(implementation = ChangePasswordRequest.class)))
            @RequestBody @Valid ChangePasswordRequest request) {
        service.updatePassword(request.getUsername(), request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok("Password updated successfully");
    }

    /**
     * Метод для установки роли администратора для пользователя.
     * @param username логин пользователя
     * @return строку с сообщением об успешной установке роли администратора
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("${spring.data.rest.admin-path}/role-admin/{username}")
    @Operation(summary = "Метод для установки роли администратора для пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<String> setRoleAdmin(
            @Parameter(description = "Имя пользователя", example = "John Doe", required = true, in = ParameterIn.PATH)
            @PathVariable @NotBlank @Size(min = 5, max = 50) String username) {
        service.setAdminRole(username);
        return ResponseEntity.ok("The admin role is set to " + username);
    }

    /**
     * Метод для продвижения пользователя.
     * @return строка с сообщением об успешном продвижении
     */
    @PutMapping("/boosted")
    @Operation(summary = "Метод для продвижения пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<String> setBoosted(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok("User " + service.setBoosted(user).getUsername() + " has received a boost");
    }

    /**
     * Метод добавляет оценку пользователю
     * @param username логин пользователя, которому требуется добавить оценку
     * @param rating оценка пользователя
     * @param sender отправитель оценки
     * @return Обновлённый пользователь, которому поставили оценку
     */
    @PostMapping("/rating")
    @Operation(summary = "Добавить оценку пользователю")
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
    public ResponseEntity<UserDto> addEvaluation(
            @Parameter(description = "Имя пользователя", example = "John Doe", required = true, in = ParameterIn.PATH)
            @RequestParam(value = "username") @NotBlank @Size(min = 5, max = 50) String username,

            @Parameter(description = "Рейтинг пользователя", example = "5", required = true, in = ParameterIn.PATH)
            @RequestParam(value = "rating") @Min(1) @Max(5) Integer rating,

            @AuthenticationPrincipal UserDetails sender) {
        return ResponseEntity.ok(mapper.toDto(service.addEvaluation(sender, username, rating)));
    }
}

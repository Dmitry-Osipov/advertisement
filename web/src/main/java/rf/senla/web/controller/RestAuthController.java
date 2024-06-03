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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rf.senla.domain.dto.JwtAuthenticationResponse;
import rf.senla.domain.dto.SignInRequest;
import rf.senla.domain.dto.SignUpRequest;
import rf.senla.domain.entity.User;
import rf.senla.domain.service.IAuthenticationService;
import rf.senla.domain.service.IEmailService;
import rf.senla.domain.service.IUserService;

/**
 * Контроллер для обработки запросов аутентификации через REST API.
 */
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
@RequestMapping("${spring.data.rest.base-path}/auth")
public class RestAuthController {
    private final IAuthenticationService authenticationService;
    private final IUserService userService;
    private final IEmailService emailService;

    /**
     * Метод для регистрации нового пользователя.
     * @param request данные запроса на регистрацию
     * @return объект с JWT-токеном для успешной регистрации
     */
    @PostMapping("/sign-up")
    @Operation(summary = "Регистрация с получением JWT-токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponse.class),
                            examples = @ExampleObject(value = "{\"token\": " +
                                    "\"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public JwtAuthenticationResponse signUp(
            @Parameter(description = "Данные регистрации", required = true,
                    content = @Content(schema = @Schema(implementation = SignUpRequest.class)))
            @Valid @RequestBody SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    /**
     * Метод для входа пользователя в систему.
     * @param request данные запроса на вход
     * @return объект с JWT-токеном для успешного входа
     */
    @PostMapping("/sign-in")
    @Operation(summary = "Авторизация с получением JWT-токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponse.class),
                            examples = @ExampleObject(value = "{\"token\": " +
                                    "\"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public JwtAuthenticationResponse signIn(
            @Parameter(description = "Данные авторизации", required = true,
                    content = @Content(schema = @Schema(implementation = SignInRequest.class)))
            @Valid @RequestBody SignInRequest request) {
        return authenticationService.signIn(request);
    }

    /**
     * Метод для отправки пользователю ссылку на восстановление пароля
     * @param username имя пользователя, которому требуется восстановить пароль
     * @param email актуальная почта пользователя, которому требуется восстановить пароль
     * @return сообщение об успешном выполнении операции
     */
    @PostMapping("/forgot-password")
    @Operation(summary = "Метод для отправки пользователю ссылку на восстановление пароля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<String> forgotPassword(
            @Parameter(description = "Имя пользователя", example = "John Doe", required = true, in = ParameterIn.QUERY)
            @RequestParam(value = "username") String username,
            @Parameter(description = "Адрес электронной почты", example = "jondoe@gmail.com",
                    required = true, in = ParameterIn.QUERY)
            @RequestParam(value = "email") String email) {
        // TODO: засунуть всю логику в один сервис
        User user = userService.getByUsername(username);
        authenticationService.createPasswordResetToken(user);
        emailService.sendResetPasswordEmail(email, user.getResetPasswordToken());
        return ResponseEntity.ok("Reset password email has been sent");
    }

    /**
     * Метод обновления пароля пользователю
     * @param token токен, высланный на почту
     * @param password новый пароль
     * @return сообщение об успешном выполнении операции
     */
    @PostMapping("/reset-password")
    @Operation(summary = "Метод обновления пароля пользователю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    public ResponseEntity<String> resetPassword(
            @Parameter(description = "Токен восстановления пароля",
                    example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...",
                    required = true, in = ParameterIn.QUERY)
            @RequestParam(value = "token") String token,
            @Parameter(description = "Пароль", example = "MY-NEW-SUPER?S3cre1_passw0rD!",
                    required = true, in = ParameterIn.QUERY)  // TODO: передавать @RequestBody
            @RequestParam(value = "password") String password) {
        // TODO: передавать jwt-токен
        authenticationService.updatePassword(authenticationService.getByResetPasswordToken(token), password);
        return ResponseEntity.ok("Password has been reset");
    }
}

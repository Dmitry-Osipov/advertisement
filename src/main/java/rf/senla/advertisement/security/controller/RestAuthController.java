package rf.senla.advertisement.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rf.senla.advertisement.security.dto.JwtAuthenticationResponse;
import rf.senla.advertisement.security.dto.SignInRequest;
import rf.senla.advertisement.security.dto.SignUpRequest;
import rf.senla.advertisement.security.service.IAuthenticationService;

/**
 * Контроллер для обработки запросов аутентификации через REST API.
 */
@RestController
@RequestMapping("${spring.data.rest.base-path}/auth")
@Validated
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class RestAuthController {
    private final IAuthenticationService authenticationService;

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
}

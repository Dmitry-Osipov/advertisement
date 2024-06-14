package rf.senla.web.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import rf.senla.web.exception.ErrorDto;
import rf.senla.domain.exception.EntityContainedException;
import rf.senla.domain.exception.ErrorMessage;
import rf.senla.domain.exception.NoEntityException;
import rf.senla.domain.exception.TechnicalException;
import rf.senla.domain.exception.EmailException;
import rf.senla.domain.exception.ResetPasswordTokenException;

import java.time.LocalDateTime;

/**
 * Контроллер обработки ошибок
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {
    /**
     * Метод возвращает информацию об ошибке типа {@link NoResourceFoundException}
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(value = "{\"message\": \" No static resource\"," +
                                    "\"description\": \"uri=/api/resource\",\"time\": " +
                                    "\"2024-06-08T15:03:51.978Z\"}")))
    })
    public ErrorDto noResourceFoundException(Exception ex, WebRequest request) {
        log.error("Ошибка перехода по эндпоинту - {}", ex.getMessage());
        return getErrorDto(ex.getMessage().substring(0, 18), request);
    }

    /**
     * Метод возвращает информацию об ошибке типа {@link HttpRequestMethodNotSupportedException}
     * @param request запрос
     * @return информация об ошибке
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "405", description = "Method Not Allowed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(value = "{\"message\": \"Incorrect http method selected\"," +
                                    "\"description\": \"uri=/api/auth/login\",\"time\": " +
                                    "\"2024-06-08T15:03:51.978Z\"}")))
    })
    public ErrorDto httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex,
                                                           WebRequest request) {
        log.error("Ошибка выбора http метода - {}", ex.getMessage());
        return getErrorDto(ErrorMessage.INCORRECT_HTTP_METHOD_SELECTED.getMessage(), request);
    }

    /**
     * Метод возвращает информацию об ошибке типа {@link NoEntityException} или {@link UsernameNotFoundException}
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NoEntityException.class, UsernameNotFoundException.class, EntityNotFoundException.class})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)))
    })
    public ErrorDto entityNotFoundException(Exception ex, WebRequest request) {
        log.error("Ошибка отсутствия сущности - {}", ex.getMessage());
        return getErrorDto(ex.getMessage(), request);
    }

    /**
     * Метод возвращает информацию об ошибке типа {@link EntityContainedException} или
     * {@link InvalidDataAccessApiUsageException}
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({EntityContainedException.class, InvalidDataAccessApiUsageException.class})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)))
    })
    public ErrorDto entityContainedException(Exception ex, WebRequest request) {
        log.error("Ошибка существования сущности - {}", ex.getMessage());
        return getErrorDto(ex.getMessage(), request);
    }

    /**
     * Метод возвращает информацию об ошибке типа {@link AccessDeniedException}
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccessDeniedException.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)))
    })
    public ErrorDto accessDeniedException(Exception ex, WebRequest request) {
        log.error("Ошибка прав доступа - {}", ex.getMessage());
        return getErrorDto(ex.getMessage(), request);
    }

    /**
     * Метод возвращает информацию об ошибке типа {@link MethodArgumentNotValidException}
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)))
    })
    public ErrorDto methodArgumentNotValidException(Exception ex, WebRequest request) {
        log.error("Ошибка валидации аргумента - {}", ex.getMessage());
        return getErrorDto(ex.getMessage(), request);
    }

    /**
     * Метод возвращает информацию об ошибке типа {@link TechnicalException}
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TechnicalException.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)))
    })
    public ErrorDto technicalException(TechnicalException ex, WebRequest request) {
        log.error("Техническая ошибка - {}", ex.getMessage());
        return getErrorDto(ex.getMessage(), request);
    }

    /**
     * Метод возвращает информацию об ошибке типа {@link ResetPasswordTokenException}
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResetPasswordTokenException.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)))
    })
    public ErrorDto resetPasswordTokenException(ResetPasswordTokenException ex, WebRequest request) {
        log.error("Ошибка валидации токена восстановления пароля - {}", ex.getMessage());
        return getErrorDto(ex.getMessage(), request);
    }

    /**
     * Метод возвращает информацию об ошибке типа {@link ConstraintViolationException}
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)))
    })
    public ErrorDto constraintViolation(Exception ex, WebRequest request) {
        log.error("Ошибка валидации jakarta {}", ex.getMessage());
        return getErrorDto(ex.getMessage(), request);
    }

    /**
     * Метод возвращает информацию об ошибке типа {@link BadCredentialsException}
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)))
    })
    public ErrorDto badCredentials(Exception ex, WebRequest request) {
        log.error("Ошибка анкетных данны {}", ex.getMessage());
        return getErrorDto(ex.getMessage(), request);
    }
    /**
     * Метод возвращает информацию об ошибке типа {@link IllegalArgumentException}
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)))
    })
    public ErrorDto illegalArgumentException(Exception ex, WebRequest request) {
        log.error("Ошибка поступивших аргументов - {}", ex.getMessage());
        return getErrorDto(ex.getMessage(), request);
    }

    /**
     * Метод возвращает информацию об ошибке типа {@link EmailException}
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(EmailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(value = "{\"message\": \"There's been an email error. Please " +
                                    "try again later\",\"description\": \"uri=/api/auth/password/forgot\",\"time\": " +
                                    "\"2024-06-08T15:03:51.978Z\"}")))
    })
    public ErrorDto emailException(Exception ex, WebRequest request) {
        log.error("Ошибка при работе с почтой - {}", ex.getMessage());
        return getErrorDto(ErrorMessage.EMAIL_EXCEPTION.getMessage(), request);
    }

    /**
     * Метод возвращает информацию о любой непредвиденной ошибке
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class),
                            examples = @ExampleObject(value = "{\"message\": \"There's been an unexpected error. " +
                                    "Please try again later\",\"description\": \"uri=/api/advertisements\"," +
                                    "\"time\": \"2024-06-08T15:03:51.978Z\"}")))
    })
    public ErrorDto allExceptionHandler(Exception ex, WebRequest request) {
        log.error("Непредвиденная ошибка - {}", ex.toString());
        ex.printStackTrace();  // TODO: удалить в конце
        return getErrorDto(ex.toString(), request);
//        return getErrorDto(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage(), request);
    }

    /**
     * Служебный метод формирует информацию об ошибке
     * @param exceptionInfo информация об ошибке
     * @param request запрос
     * @return информация об ошибке
     */
    private static ErrorDto getErrorDto(String exceptionInfo, WebRequest request) {
        return ErrorDto.builder()
                .message(exceptionInfo)
                .description(request.getDescription(false))
                .time(LocalDateTime.now())
                .build();
    }
}

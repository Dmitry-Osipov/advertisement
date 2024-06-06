package rf.senla.web.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
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
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto noResourceFoundException(Exception ex, WebRequest request) {
        log.error("Ошибка перехода по эндпоинту - {}", ex.getMessage());
        return getErrorDto(ex.getMessage().substring(0, 18), request);
    }

    /**
     * Метод возвращает информацию об ошибке типа {@link HttpRequestMethodNotSupportedException}
     * @param request запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
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
    @ExceptionHandler({NoEntityException.class, UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
    @ExceptionHandler({EntityContainedException.class, InvalidDataAccessApiUsageException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
    @ExceptionHandler(TechnicalException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
    @ExceptionHandler(ResetPasswordTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto badCredentials(Exception ex, WebRequest request) {
        log.error("Ошибка анкетных данны {}", ex.getMessage());
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
    public ErrorDto emailException(EmailException ex, WebRequest request) {
        log.error("Ошибка при работе с почтой - {}", ex.getMessage());
        return getErrorDto(ex.getMessage(), request);
    }

    /**
     * Метод возвращает информацию о любой непредвиденной ошибке
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto allExceptionHandler(Exception ex, WebRequest request) {
        log.error("Непредвиденная ошибка - {}", ex.toString());
        return getErrorDto(ex.toString(), request);  // TODO: заменить в самом конце на абстрактное сообщение об ошибке
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

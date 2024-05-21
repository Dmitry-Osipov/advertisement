package rf.senla.advertisement.domain.controller;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import rf.senla.advertisement.domain.dto.ErrorDto;
import rf.senla.advertisement.domain.exception.EntityContainedException;
import rf.senla.advertisement.domain.exception.NoEntityException;

import java.time.LocalDateTime;

/**
 * Контроллер обработки ошибок
 */
@RestControllerAdvice
public class RestExceptionHandler {
    /**
     * Метод возвращает информацию об ошибке типа {@link NoEntityException} или {@link UsernameNotFoundException}
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ExceptionHandler({NoEntityException.class, UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto entityNotFoundException(Exception ex, WebRequest request) {
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
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto entityContainedException(Exception ex, WebRequest request) {
        return getErrorDto(ex.getMessage(), request);
    }

    /**
     * Метод возвращает информацию об ошибке типа {@link AccessDeniedException}
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto accessDeniedException(Exception ex, WebRequest request) {
        return getErrorDto(ex.getMessage(), request);
    }

    /**
     * Метод возвращает информацию об ошибке типа {@link NoResourceFoundException}
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto noResourceFoundException(Exception ex, WebRequest request) {
        return getErrorDto(ex.getMessage().substring(0, 18), request);
    }

    /**
     * Метод возвращает информацию об ошибке типа {@link HttpRequestMethodNotSupportedException}
     * @param request запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto httpRequestMethodNotSupportedException(WebRequest request) {
        return getErrorDto("Incorrect http method selected", request);
    }

    /**
     * Метод возвращает информацию об ошибке типа {@link MethodArgumentNotValidException}
     * @param ex ошибка
     * @param request запрос
     * @return информация об ошибке
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto methodArgumentNotValidException(Exception ex, WebRequest request) {
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

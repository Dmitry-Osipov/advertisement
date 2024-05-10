package rf.senla.advertisement.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс предназначен для отработки сообщений об ошибке.
 */
@Getter
@AllArgsConstructor
public enum ErrorMessage {
    USER_IS_NOT_ADMIN_OR_AUTHOR("The user is not the administrator or author of the advertisement"),
    NO_ADVERTISEMENT_FOUND("The advertisement could not be found"),
    NO_USER_FOUND("The user could not be found");

    private final String message;
}

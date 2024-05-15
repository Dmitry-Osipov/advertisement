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
    NO_USER_FOUND("The user could not be found"),
    PRICE_IS_NEGATIVE("The price cannot be negative"),
    MIN_PRICE_IS_HIGHEST("The minimum price cannot be higher than the maximum price"),
    USER_IS_NOT_AUTHOR("The user is not author of the message");

    private final String message;
}

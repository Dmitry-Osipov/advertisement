package rf.senla.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс предназначен для отработки сообщений об ошибке.
 */
@Getter
@AllArgsConstructor
public enum ErrorMessage {
    NO_ADVERTISEMENT_FOUND("The advertisement could not be found"),
    NO_MESSAGE_FOUND("The message could not be found"),
    NO_COMMENT_FOUND("The comment could not be found"),
    PRICE_IS_NEGATIVE("The price cannot be negative"),
    MIN_PRICE_IS_HIGHEST("The minimum price cannot be higher than the maximum price"),
    ADVERTISEMENT_ALREADY_EXISTS("The advertisement already exists"),
    MESSAGE_ALREADY_EXISTS("The message already exists"),
    COMMENT_ALREADY_EXISTS("The comment already exists"),
    PASSWORDS_DO_NOT_MATCH("Current and transferred passwords do not match"),
    USER_NOT_FOUND("User not found"),
    USERNAME_ALREADY_EXISTS("Username already exists"),
    EMAIL_ALREADY_EXISTS("Email already exists"),
    TOKEN_EXPIRED("Token expired"),
    INCORRECT_HTTP_METHOD_SELECTED("Incorrect http method selected");

    private final String message;
}

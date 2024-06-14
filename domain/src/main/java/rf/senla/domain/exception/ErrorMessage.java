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
    MIN_PRICE_IS_HIGHEST("The minimum price cannot be higher than the maximum price"),
    ADVERTISEMENT_ALREADY_EXISTS("The advertisement already exists"),
    MESSAGE_ALREADY_EXISTS("The message already exists"),
    COMMENT_ALREADY_EXISTS("The comment already exists"),
    PASSWORDS_DO_NOT_MATCH("Current and transferred passwords do not match"),
    USER_NOT_FOUND("User not found"),
    USERNAME_ALREADY_EXISTS("Username already exists"),
    EMAIL_ALREADY_EXISTS("Email already exists"),
    TOKEN_EXPIRED("Token expired"),
    INCORRECT_HTTP_METHOD_SELECTED("Incorrect http method selected"),
    SENDER_ALREADY_VOTED("The sender and receiver pair is already in place"),
    SENDER_MISMATCH("The sender and current user does not match"),
    EMAIL_EXCEPTION("There's been an email error. Please try again later"),
    INTERNAL_SERVER_ERROR("There's been an unexpected error. Please try again later");

    private final String message;
}

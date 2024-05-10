package rf.senla.advertisement.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс предназначен для отработки сообщений об ошибке.
 */
@Getter
@AllArgsConstructor
public enum ErrorMessage {
    USERS_DO_NOT_MATCH("Current and transferred users do not match"),
    PASSWORDS_DO_NOT_MATCH("Current and transferred passwords do not match"),
    USER_NOT_FOUND("User not found"),
    USERNAME_ALREADY_EXISTS("Username already exists"),
    EMAIL_ALREADY_EXISTS("Email already exists");

    private final String message;
}

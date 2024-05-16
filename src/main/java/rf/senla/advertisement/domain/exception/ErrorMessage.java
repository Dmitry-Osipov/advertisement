package rf.senla.advertisement.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс предназначен для отработки сообщений об ошибке.
 */
@Getter
@AllArgsConstructor
public enum ErrorMessage {
    NO_ADVERTISEMENT_FOUND("The advertisement could not be found"),
    PRICE_IS_NEGATIVE("The price cannot be negative"),
    MIN_PRICE_IS_HIGHEST("The minimum price cannot be higher than the maximum price");

    private final String message;
}

package rf.senla.advertisement.security.exception;

/**
 * Исключение, выбрасываемое при ошибке работы с токеном восстановления пароля
 */
public class ResetPasswordTokenException extends RuntimeException {
    public ResetPasswordTokenException(String message) {
        super(message);
    }
}

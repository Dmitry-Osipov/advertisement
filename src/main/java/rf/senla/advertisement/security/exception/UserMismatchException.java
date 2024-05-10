package rf.senla.advertisement.security.exception;

/**
 * Исключение, сигнализирующее о несоответствии пользователя.
 */
public class UserMismatchException extends RuntimeException {
    public UserMismatchException(String message) {
        super(message);
    }
}

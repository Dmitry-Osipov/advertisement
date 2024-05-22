package rf.senla.advertisement.security.exception;

/**
 * Исключение, выбрасываемое при ошибке работы почты
 */
public class EmailException extends RuntimeException {
    public EmailException(Throwable cause) {
        super(cause);
    }
}

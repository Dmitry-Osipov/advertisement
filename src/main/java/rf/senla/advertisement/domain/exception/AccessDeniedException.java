package rf.senla.advertisement.domain.exception;

/**
 * Исключение, выбрасываемое, когда доступ запрещен.
 */
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}

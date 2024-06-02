package rf.senla.domain.exception;

/**
 * Исключение, выбрасываемое при отсутствии сущности.
 */
public class NoEntityException extends RuntimeException {
    public NoEntityException(String message) {
        super(message);
    }
}

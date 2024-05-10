package rf.senla.advertisement.domain.exception;

/**
 * Исключение, выбрасываемое, когда сущность уже содержится в системе.
 */
public class EntityContainedException extends RuntimeException {
    public EntityContainedException(String message) {
        super(message);
    }
}

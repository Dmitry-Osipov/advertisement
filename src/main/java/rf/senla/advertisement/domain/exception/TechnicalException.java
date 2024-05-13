package rf.senla.advertisement.domain.exception;

/**
 * Ошибка, выбрасываемая при технических неисправностях.
 */
public class TechnicalException extends RuntimeException {
    public TechnicalException(String message) {
        super(message);
    }
}

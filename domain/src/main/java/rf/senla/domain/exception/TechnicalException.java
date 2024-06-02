package rf.senla.domain.exception;

/**
 * Ошибка, выбрасываемая при технических неисправностях.
 */
public class TechnicalException extends RuntimeException {
    public TechnicalException(String message) {
        super(message);
    }
}

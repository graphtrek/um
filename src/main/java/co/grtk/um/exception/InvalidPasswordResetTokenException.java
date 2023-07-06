package co.grtk.um.exception;

/**
 * @author Sampson Alfred
 */

public class InvalidPasswordResetTokenException extends RuntimeException {
    public InvalidPasswordResetTokenException(String message) {
        super(message);
    }
}

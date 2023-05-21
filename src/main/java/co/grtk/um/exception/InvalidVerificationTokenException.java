package co.grtk.um.exception;

/**
 * @author Sampson Alfred
 */

public class InvalidVerificationTokenException extends RuntimeException {
    public InvalidVerificationTokenException(String message) {
        super(message);
    }
}

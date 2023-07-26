package co.grtk.um.exception;

/**
 * @author Sampson Alfred
 */

public class InvalidRegistrationTokenException extends RuntimeException {
    public InvalidRegistrationTokenException(String message) {
        super(message);
    }
}

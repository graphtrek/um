package co.grtk.um.exception;

/**
 * @author Sampson Alfred
 */

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

package co.grtk.um.exception;

/**
 * @author Sampson Alfred
 */

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

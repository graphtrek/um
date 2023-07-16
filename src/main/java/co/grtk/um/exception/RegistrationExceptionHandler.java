package co.grtk.um.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RegistrationExceptionHandler {
    private static final String ERROR = "error";
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public Map<String, String> userAlreadyExists(UserAlreadyExistsException ex){
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidTwoFactorVerificationCode.class)
    public Map<String, String> invalidTwoFactorVerificationCode(InvalidTwoFactorVerificationCode ex){
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        return error;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException.class)
    public Map<String, String> userNotFound(UserNotFoundException ex){
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidVerificationTokenException.class)
    public Map<String, String> invalidVerificationToken(InvalidVerificationTokenException ex){
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPasswordResetTokenException.class)
    public Map<String, String> invalidPasswordRestToken(InvalidPasswordResetTokenException ex){
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UmException.class)
    public Map<String, String> umException(UmException ex){
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        return error;
    }
}

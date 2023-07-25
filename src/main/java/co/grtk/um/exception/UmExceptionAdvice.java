package co.grtk.um.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class UmExceptionAdvice {
    private static final String ERROR = "error";
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        log.error("MethodArgumentNotValidException ex:{}", ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public Map<String, String> userAlreadyExists(UserAlreadyExistsException ex){
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        log.error("UserAlreadyExistsException ex:{}", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidTwoFactorVerificationCode.class)
    public Map<String, String> invalidTwoFactorVerificationCode(InvalidTwoFactorVerificationCode ex){
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        log.error("InvalidTwoFactorVerificationCode ex:{}", ex.getMessage());
        return error;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException.class)
    public Map<String, String> userNotFound(UserNotFoundException ex){
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        log.error("UserNotFoundException ex:{}", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidVerificationTokenException.class)
    public Map<String, String> invalidVerificationToken(InvalidVerificationTokenException ex){
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        log.error("InvalidVerificationTokenException ex:{}", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPasswordResetTokenException.class)
    public Map<String, String> invalidPasswordRestToken(InvalidPasswordResetTokenException ex){
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        log.error("InvalidPasswordResetTokenException ex:{}", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UmException.class)
    public Map<String, String> umException(UmException ex){
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        log.error("UmException ex:{}", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenRefreshException.class)
    public Map<String, String> tokenRefreshException(TokenRefreshException ex){
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        log.error("TokenRefreshException ex:{}", ex.getMessage());
        return error;
    }

}

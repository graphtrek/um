package co.grtk.um.exception;

public class InvalidTwoFactorVerificationCode extends RuntimeException {
    public InvalidTwoFactorVerificationCode(String message) {
        super(message);
    }
}

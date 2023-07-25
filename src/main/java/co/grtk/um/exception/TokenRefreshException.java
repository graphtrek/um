package co.grtk.um.exception;

public class TokenRefreshException extends RuntimeException {

  public TokenRefreshException(String token, String message) {
    super(String.format("Failed for refreshToken [%s]: %s", token, message));}
}

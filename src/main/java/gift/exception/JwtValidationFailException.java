package gift.exception;

public class JwtValidationFailException extends RuntimeException {
  public JwtValidationFailException(String message) {
    super(message);
  }
}

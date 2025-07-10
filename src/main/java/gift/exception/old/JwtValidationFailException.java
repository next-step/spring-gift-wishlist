package gift.exception.old;

public class JwtValidationFailException extends RuntimeException {
  public JwtValidationFailException(String message) {
    super(message);
  }
}

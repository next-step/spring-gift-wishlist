package gift.auth.exception;

public class InvalidTokenException extends RuntimeException {

  private final AuthErrorCode errorCode;

  public InvalidTokenException() {
    super(AuthErrorCode.INVALID_TOKEN.getErrorMessage());
    this.errorCode = AuthErrorCode.INVALID_TOKEN;
  }

  public InvalidTokenException(String message) {
    super(message);
    this.errorCode = AuthErrorCode.INVALID_TOKEN;
  }

  public AuthErrorCode getErrorCode() {
    return errorCode;
  }
}

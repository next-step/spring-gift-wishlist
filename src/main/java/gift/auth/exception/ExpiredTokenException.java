package gift.auth.exception;

public class ExpiredTokenException extends RuntimeException {

  private final AuthErrorCode errorCode;

  public ExpiredTokenException() {
    super(AuthErrorCode.EXPIRED_TOKEN.getErrorMessage());
    this.errorCode = AuthErrorCode.EXPIRED_TOKEN;
  }

  public ExpiredTokenException(String message) {
    super(message);
    this.errorCode = AuthErrorCode.EXPIRED_TOKEN;
  }

  public AuthErrorCode getErrorCode() {
    return errorCode;
  }
}

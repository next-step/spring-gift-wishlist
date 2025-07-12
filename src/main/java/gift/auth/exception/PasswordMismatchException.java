package gift.auth.exception;

public class PasswordMismatchException extends RuntimeException {

  private final AuthErrorCode errorCode;

  public PasswordMismatchException() {
    super(AuthErrorCode.PASSWORD_MISMATCH.getErrorMessage());
    errorCode = AuthErrorCode.PASSWORD_MISMATCH;
  }

  public AuthErrorCode getErrorCode() {
    return errorCode;
  }
}

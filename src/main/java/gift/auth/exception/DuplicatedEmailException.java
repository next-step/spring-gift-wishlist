package gift.auth.exception;

public class DuplicatedEmailException extends RuntimeException {

  private final AuthErrorCode errorCode;

  public DuplicatedEmailException() {
    super(AuthErrorCode.DUPLICATED_EMAIL.getErrorMessage());
    this.errorCode = AuthErrorCode.DUPLICATED_EMAIL;
  }

  public DuplicatedEmailException(String message) {
    super(message);
    this.errorCode = AuthErrorCode.DUPLICATED_EMAIL;
  }

  public AuthErrorCode getErrorCode() {
    return errorCode;
  }
}

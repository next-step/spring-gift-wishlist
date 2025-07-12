package gift.member.exception;

public class MemberNotFoundException extends RuntimeException {

  private final MemberErrorCode errorCode;

  public MemberNotFoundException(MemberErrorCode errorCode) {
    super(errorCode.getErrorMessage());
    this.errorCode = errorCode;
  }

  public MemberNotFoundException(String message, MemberErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public MemberErrorCode getErrorCode() {
    return errorCode;
  }
}

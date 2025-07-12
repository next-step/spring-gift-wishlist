package gift.wishlist.exception;

public class WishItemNotFoundException extends RuntimeException {

  private final WishItemErrorCode errorCode;

  public WishItemNotFoundException(WishItemErrorCode errorCode) {
    super(errorCode.getErrorMessage());
    this.errorCode = errorCode;
  }

  public WishItemNotFoundException(String message, WishItemErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public WishItemErrorCode getErrorCode() {
    return errorCode;
  }
}

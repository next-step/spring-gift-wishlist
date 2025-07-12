package gift.wishlist.exception;

public class WishItemAlreadyExistsException extends RuntimeException {

  private final WishItemErrorCode errorCode;

  public WishItemAlreadyExistsException(WishItemErrorCode errorCode) {
    super(errorCode.getErrorMessage());
    this.errorCode = errorCode;
  }

  public WishItemAlreadyExistsException(String message, WishItemErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public WishItemErrorCode getErrorCode() {
    return errorCode;
  }
}

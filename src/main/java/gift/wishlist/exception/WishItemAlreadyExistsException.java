package gift.wishlist.exception;

public class WishItemAlreadyExistsException extends RuntimeException {

  private final WishItemErrorCode errorCode;

  public WishItemAlreadyExistsException() {
    super(WishItemErrorCode.WISH_ITEM_ALREADY_EXISTS.getErrorMessage());
    this.errorCode = WishItemErrorCode.WISH_ITEM_ALREADY_EXISTS;
  }

  public WishItemAlreadyExistsException(String message) {
    super(message);
    this.errorCode = WishItemErrorCode.WISH_ITEM_ALREADY_EXISTS;
  }

  public WishItemErrorCode getErrorCode() {
    return errorCode;
  }
}

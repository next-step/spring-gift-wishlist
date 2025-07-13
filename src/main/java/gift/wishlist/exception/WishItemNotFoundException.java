package gift.wishlist.exception;

public class WishItemNotFoundException extends RuntimeException {

  private final WishItemErrorCode errorCode;

  public WishItemNotFoundException() {
    super(WishItemErrorCode.WISH_ITEM_NOT_FOUND.getErrorMessage());
    this.errorCode = WishItemErrorCode.WISH_ITEM_NOT_FOUND;
  }

  public WishItemNotFoundException(String message) {
    super(message);
    this.errorCode = WishItemErrorCode.WISH_ITEM_NOT_FOUND;
  }

  public WishItemErrorCode getErrorCode() {
    return errorCode;
  }
}

package gift.product.exception;

public class InvalidProductNameException extends RuntimeException {

  private final ProductErrorCode errorCode;

  public InvalidProductNameException() {
    super(ProductErrorCode.INVALID_PRODUCT_NAME.getErrorMessage());
    this.errorCode = ProductErrorCode.INVALID_PRODUCT_NAME;
  }

  public InvalidProductNameException(String message) {
    super(message);
    this.errorCode = ProductErrorCode.INVALID_PRODUCT_NAME;
  }

  public ProductErrorCode getErrorCode() {
    return errorCode;
  }
}

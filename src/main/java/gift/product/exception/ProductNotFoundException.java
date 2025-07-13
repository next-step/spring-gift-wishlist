package gift.product.exception;

public class ProductNotFoundException extends RuntimeException {

  private final ProductErrorCode errorCode;

  public ProductNotFoundException() {
    super(ProductErrorCode.PRODUCT_NOT_FOUND.getErrorMessage());
    this.errorCode = ProductErrorCode.PRODUCT_NOT_FOUND;
  }

  public ProductErrorCode getErrorCode() {
    return errorCode;
  }
}

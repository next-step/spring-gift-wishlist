package gift.product.exception;


public class InvalidProductSortFieldException extends RuntimeException {

  private final ProductErrorCode errorCode;

  public InvalidProductSortFieldException(ProductErrorCode errorCode) {
    super(errorCode.getErrorMessage());
    this.errorCode = errorCode;
  }

  public ProductErrorCode getErrorCode() {
    return errorCode;
  }
}

package gift.product.exception;


public class InvalidProductSortFieldException extends RuntimeException {

  private final ProductErrorCode errorCode;

  public InvalidProductSortFieldException() {
    super(ProductErrorCode.INVALID_SORT_FIELD_ERROR.getErrorMessage());
    this.errorCode = ProductErrorCode.INVALID_SORT_FIELD_ERROR;
  }

  public ProductErrorCode getErrorCode() {
    return errorCode;
  }
}

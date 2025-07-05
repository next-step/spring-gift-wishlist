package gift.global.exception;


public class ProductInvalidSortFieldException extends RuntimeException {

  private final GlobalErrorCode globalErrorCode;

  public ProductInvalidSortFieldException(GlobalErrorCode globalErrorCode) {
    super(globalErrorCode.getErrorMessage());
    this.globalErrorCode = globalErrorCode;
  }

  public GlobalErrorCode getErrorCode() {
    return globalErrorCode;
  }
}

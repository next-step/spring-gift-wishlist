package gift.product.exception;

public class InvalidProductNameException extends RuntimeException{
  private final ProductErrorCode errorCode;

  public InvalidProductNameException(ProductErrorCode errorCode){
    super(errorCode.getErrorMessage());
    this.errorCode = errorCode;
  }

  public InvalidProductNameException(String message, ProductErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public ProductErrorCode getErrorCode() {
    return errorCode;
  }
}

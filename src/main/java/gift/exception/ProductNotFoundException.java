package gift.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends BusinessException{
  public ProductNotFoundException() {
    super(ErrorCode.PRODUCT_NOT_FOUND);
  }
}
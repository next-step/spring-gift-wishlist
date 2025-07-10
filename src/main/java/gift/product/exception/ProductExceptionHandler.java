package gift.product.exception;

import gift.global.exception.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(ProductExceptionHandler.class);

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleProductNotFoundException(
      ProductNotFoundException exception) {
    logger.error("Product not found: {}", exception.getMessage());
    return createErrorResponse(exception.getErrorCode(), exception);
  }

  @ExceptionHandler(InvalidProductNameException.class)
  public ResponseEntity<ErrorResponse> handleInvalidProductNameException(
      InvalidProductNameException exception) {
    logger.error("Invalid product name: {}", exception.getMessage());
    return createErrorResponse(exception.getErrorCode(), exception);
  }

  @ExceptionHandler(InvalidProductSortFieldException.class)
  public ResponseEntity<ErrorResponse> handleSortFieldException(
      InvalidProductSortFieldException exception) {
    return createErrorResponse(exception.getErrorCode(), exception);
  }

  private static ResponseEntity<ErrorResponse> createErrorResponse(ProductErrorCode errorCode,
      Exception exception) {
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode, exception.getMessage()));
  }
}
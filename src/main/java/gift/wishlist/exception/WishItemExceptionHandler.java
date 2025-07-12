package gift.wishlist.exception;

import gift.global.exception.dto.ErrorResponse;
import gift.product.exception.ProductExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WishItemExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(ProductExceptionHandler.class);

  @ExceptionHandler(WishItemNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleWishItemNotFoundException(
      WishItemNotFoundException exception) {
    logger.error("WishItem not found: {}", exception.getMessage());
    return createErrorResponse(exception.getErrorCode());
  }

  @ExceptionHandler(WishItemAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleWishItemAlreadyExistsException(
      WishItemAlreadyExistsException exception) {
    logger.error("WishItem already exists: {}", exception.getMessage());
    return createErrorResponse(exception.getErrorCode());
  }

  private static ResponseEntity<ErrorResponse> createErrorResponse(WishItemErrorCode errorCode) {
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode));
  }

  private static ResponseEntity<ErrorResponse> createErrorResponse(WishItemErrorCode errorCode,
      Exception exception) {
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode, exception.getMessage()));
  }

}

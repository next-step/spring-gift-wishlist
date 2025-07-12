package gift.member.exception;

import gift.global.exception.dto.ErrorResponse;
import gift.product.exception.ProductErrorCode;
import gift.product.exception.ProductExceptionHandler;
import gift.product.exception.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(ProductExceptionHandler.class);

  @ExceptionHandler(MemberNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleMemberNotFoundException(
      MemberNotFoundException exception) {
    logger.error("Member not found: {}", exception.getMessage());
    return createErrorResponse(exception.getErrorCode());
  }

  private static ResponseEntity<ErrorResponse> createErrorResponse(MemberErrorCode errorCode) {
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode));
  }
  private static ResponseEntity<ErrorResponse> createErrorResponse(MemberErrorCode errorCode,
      Exception exception) {
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode, exception.getMessage()));
  }

}

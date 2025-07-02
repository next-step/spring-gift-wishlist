package gift.global.exception;

import gift.global.exception.dto.ErrorResponse;
import gift.product.exception.ProductNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    List<Map<String, String>> invalidParams = exception.getFieldErrors().stream()
        .map(fieldError -> Map.of(
            "name", fieldError.getField(),
            "value", fieldError.getRejectedValue().toString(),
            "reason", fieldError.getDefaultMessage()
        ))
        .toList();

    Map<String, Object> additionalInfo = Map.of("invalid-params", invalidParams);

    return createErrorResponse(ErrorCode.INVALID_ARGUMENT_ERROR, exception, additionalInfo);
  }
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
    List<Map<String, String>> invalidParams = exception.getConstraintViolations().stream()
        .map(violation -> Map.of(
            "name", extractFieldName(violation.getPropertyPath().toString()),
            "value", violation.getInvalidValue().toString(),
            "reason", violation.getMessage()
        ))
        .toList();

    Map<String, Object> additionalInfo = Map.of("invalid-params", invalidParams);

    return createErrorResponse(ErrorCode.INVALID_ARGUMENT_ERROR, exception, additionalInfo);
  }

  private String extractFieldName(String propertyPath) {
    return propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(Exception exception) {
    return createErrorResponse(ErrorCode.INVALID_ARGUMENT_ERROR, exception);
  }

  @ExceptionHandler(InvalidSortFieldException.class)
  public ResponseEntity<ErrorResponse> handleSortFieldException(InvalidSortFieldException exception) {
    return createErrorResponse(exception.getErrorCode(),exception);
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(
      ProductNotFoundException exception) {
    return createErrorResponse(exception.getErrorCode(),exception);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception exception) {
    return createErrorResponse(ErrorCode.INTERNAL_ERROR,exception);
  }

  private static ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode errorCode, Exception exception) {
    logger.error("Exception occurred: {}", exception.getMessage());
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode));
  }

  private static ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode errorCode, Exception exception, Map<String, Object> additionalInfo) {
    logger.error("Exception occurred: {}", exception.getMessage());
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode, additionalInfo));
  }
}

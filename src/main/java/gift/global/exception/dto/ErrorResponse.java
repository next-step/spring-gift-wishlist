package gift.global.exception.dto;

import gift.global.exception.ErrorCode;
import gift.global.exception.GlobalErrorCode;
import java.util.Collections;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public record ErrorResponse(
    String errorCode,
    String errorMessage,
    Map<String, Object> extras
) {

  private ErrorResponse(String errorCode, String errorMessage) {
    this(errorCode, errorMessage, Collections.emptyMap());
  }

  public static ErrorResponse from(ErrorCode errorCode) {
    return new ErrorResponse(errorCode.getErrorCode(), errorCode.getErrorMessage());
  }

  public static ErrorResponse from(ErrorCode errorCode, String errorMessage) {
    return new ErrorResponse(errorCode.getErrorCode(), errorMessage);
  }

  public static ErrorResponse from(ErrorCode errorCode, Map<String, Object> extras) {
    return new ErrorResponse(errorCode.getErrorCode(), errorCode.getErrorMessage(), extras);
  }

  public static ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode errorCode) {
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode));
  }

  public static ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode errorCode,
      Exception exception) {
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode, exception.getMessage()));
  }

  public static ResponseEntity<ErrorResponse> createErrorResponse(GlobalErrorCode errorCode,
      Exception exception, Map<String, Object> additionalInfo) {
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ErrorResponse.from(errorCode, additionalInfo));
  }
}
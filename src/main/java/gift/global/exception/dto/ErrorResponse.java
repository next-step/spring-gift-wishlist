package gift.global.exception.dto;

import gift.global.exception.ErrorCode;
import gift.global.exception.GlobalErrorCode;
import java.util.Collections;
import java.util.Map;

public record ErrorResponse(
    String errorCode,
    String errorMessage,
    Map<String, Object> extras
) {

  public ErrorResponse(String errorCode, String errorMessage) {
    this(errorCode, errorMessage, Collections.emptyMap());
  }

  public static ErrorResponse from(ErrorCode globalErrorCode) {
    return new ErrorResponse(globalErrorCode.getErrorCode(), globalErrorCode.getErrorMessage());
  }

  public static ErrorResponse from(ErrorCode globalErrorCode, String errorMessage){
    return new ErrorResponse(globalErrorCode.getErrorCode(),errorMessage);
  }

  public static ErrorResponse from(ErrorCode globalErrorCode, Map<String, Object> extras) {
    return new ErrorResponse(globalErrorCode.getErrorCode(), globalErrorCode.getErrorMessage(), extras);
  }
}
package gift.global.exception;

import org.springframework.http.HttpStatus;

public enum GlobalErrorCode implements ErrorCode {
  INVALID_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "G001", "요청 값이 유효하지 않습니다"),
  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G002", "내부 서버 오류가 발생했습니다"),
  INVALID_SORT_FIELD_ERROR(HttpStatus.BAD_REQUEST, "G003", "페이징 정렬 필드 값이 올바르지 않습니다"),
  UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED, "G004", "인증이 필요합니다"),
  FORBIDDEN_ERROR(HttpStatus.FORBIDDEN, "G005", "접근 권한이 없습니다");

  private final HttpStatus status;
  private final String errorCode;
  private final String errorMessage;

  GlobalErrorCode(HttpStatus status, String errorCode, String errorMessage) {
    this.status = status;
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  @Override
  public HttpStatus getStatus() {
    return status;
  }

  @Override
  public String getErrorCode() {
    return errorCode;
  }

  @Override
  public String getErrorMessage() {
    return errorMessage;
  }
}

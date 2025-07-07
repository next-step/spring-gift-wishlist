package gift.exception;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

public enum ErrorCode {
  //Bean Validation!
  VALIDATION_FAILED("VALIDATION_FAILED", "입력값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

  //요구사항 field validation!
  INVALID_PRODUCT_NAME("INVALID_PRODUCT_NAME", "상품명이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
  INVALID_PRODUCT_PRICE("INVALID_PRODUCT_PRICE", "상품 가격이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
  INVALID_IMAGE_URL("INVALID_IMAGE_URL", "이미지 URL이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
  KAKAO_APPROVAL_REQUIRED("KAKAO_APPROVAL_REQUIRED","카카오 관련 상품명은 MD승인이 필요합니다.",HttpStatus.BAD_REQUEST),

  INVALID_LOGIN("INVALID_LOGIN", "이메일 또는 비밀번호가 일치하지 않습니다.",HttpStatus.FORBIDDEN),
  USER_NOT_FOUND("USER_NOT_FOUND","존재하지 않는 사용자입니다.",HttpStatus.NOT_FOUND),
  PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND","존재하지 않는 상품입니다.",HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;

  ErrorCode(String code, String message, HttpStatus status) {
    this.code = code;
    this.message = message;
    this.status = status;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public HttpStatus getStatus() {
    return status;
  }
}

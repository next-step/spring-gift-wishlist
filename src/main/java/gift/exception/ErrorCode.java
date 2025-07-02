package gift.exception;

public enum ErrorCode {

  INVALID_PRODUCT_NAME("INVALID_PRODUCT_NAME", "상품명이 올바르지 않습니다."),
  INVALID_PRODUCT_PRICE("INVALID_PRODUCT_PRICE", "상품 가격이 올바르지 않습니다."),
  INVALID_IMAGE_URL("INVALID_IMAGE_URL", "이미지 URL이 올바르지 않습니다.");

  private final String code;
  private final String message;

  ErrorCode(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}

package gift.exception;

public enum ErrorCode {

    // 400 Bad Request - 유효성 검사 실패
    NAME_BLANK("E001", "상품 이름은 비어 있을 수 없습니다."),
    NAME_TOO_LONG("E002", "상품 이름은 최대 15자까지 입력할 수 있습니다."),
    NAME_INVALID_CHARACTERS("E003", "상품 이름에는 허용되지 않은 특수문자가 포함되어 있습니다."),
    NAME_CONTAINS_KAKAO("E004", "\"카카오\"가 포함된 상품 이름은 사용할 수 없습니다."),

    PRICE_NEGATIVE("E005", "가격은 0 이상이어야 합니다."),
    IMGURL_BLANK("E006", "이미지 URL은 비어 있을 수 없습니다."),

    // 404 Not Found
    PRODUCT_NOT_FOUND("E100", "해당 상품을 찾을 수 없습니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR("E999", "서버 오류가 발생했습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }
}

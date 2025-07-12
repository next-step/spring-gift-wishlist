package gift.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    USER_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 이메일을 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    INVALID_PRODUCT_DATA(HttpStatus.BAD_REQUEST, "상품 데이터가 유효하지 않습니다."),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다."),
    INVALID_PRICE_FORMAT(HttpStatus.BAD_REQUEST, "price가 숫자 형식이 아닙니다."),
    PRICE_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "price 값이 유효 범위를 벗어났습니다."),
    USER_PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
    
    private final HttpStatus status;
    private final String description;

    ErrorCode(HttpStatus status, String description) {
        this.status = status;
        this.description = description;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}

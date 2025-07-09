package gift.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "P001", "상품 입력값이 유효하지 않습니다."),
    NO_SUPPORTED_SHA256_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "E001", "SHA-256 알고리즘을 지원하지 않습니다.");

    private HttpStatus status;
    private String errorCode;
    private String message;

    ErrorCode(HttpStatus status, String errorCode, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        return this.message;
    }
}

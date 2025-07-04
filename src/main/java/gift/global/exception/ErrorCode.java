package gift.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 400 BadRequest
    ITEM_KEYWORD_INVALID(40000, HttpStatus.BAD_REQUEST, "'카카오' 단어는 MD와 협의 후 사용 가능합니다."),

    // 404 NOT FOUND
    ITEM_NOT_FOUND(40400, HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다.");

    private final int code; // 커스텀 에러 코드
    private final HttpStatus httpStatus; // HTTP 상태 코드
    private final String message; // 에러 메시지

    ErrorCode(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}

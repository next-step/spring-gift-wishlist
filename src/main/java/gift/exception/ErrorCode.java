package gift.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    Forbidden(HttpStatus.FORBIDDEN, "개시 중단됨, 개시를 원하시면 담당 MD와 협의해 주세요");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

package gift.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    Notfound(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다");

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

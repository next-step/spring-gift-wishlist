package gift.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    Notfound(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다"),
    Unauthorized(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다"),
    AlreadyRegistered(HttpStatus.FORBIDDEN, "이미 가입한 이메일입니다"),
    NotRegisterd(HttpStatus.NOT_FOUND, "해당 이메일은 가입하지 않았습니다");

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

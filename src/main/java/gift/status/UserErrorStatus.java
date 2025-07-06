package gift.status;

import org.springframework.http.HttpStatus;

public enum UserErrorStatus{
    NO_USER("UE001", "해당 User 를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("UE002", "비밀번호가 맞지 않습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

    UserErrorStatus(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getErrorMessage(){
        return "[" + code + "] " + message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

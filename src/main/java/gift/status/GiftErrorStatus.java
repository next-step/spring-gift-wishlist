package gift.status;

import org.springframework.http.HttpStatus;

public enum GiftErrorStatus {
    NO_GIFT("GE001", "상품이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    WRONG_CHARACTER("GE002", "특수문자는 ( ), [ ], +, -, &, /, _ 만 허용됩니다.", HttpStatus.BAD_REQUEST),
    NOT_ACCEPTED("GE003", "상품에 대해 MD 의 허가가 필요합니다.", HttpStatus.FORBIDDEN),
    NO_VALUE("GE004", "값이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

    GiftErrorStatus(String code, String message, HttpStatus status) {
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

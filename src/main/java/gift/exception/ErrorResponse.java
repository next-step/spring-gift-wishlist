package gift.exception;

public class ErrorResponse {

    private final String code;
    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.code();
        this.message = errorCode.message();
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}


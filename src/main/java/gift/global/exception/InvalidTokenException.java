package gift.global.exception;

public class InvalidTokenException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public InvalidTokenException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode getErrorCode(){
        return this.errorCode;
    }
}

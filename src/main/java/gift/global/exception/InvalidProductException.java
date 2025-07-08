package gift.global.exception;

public class InvalidProductException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public InvalidProductException(ErrorCode errorCode, String message) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}

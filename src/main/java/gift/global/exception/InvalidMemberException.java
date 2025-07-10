package gift.global.exception;

public class InvalidMemberException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String message;

    public InvalidMemberException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode getErrorCode(){
        return this.errorCode;
    }

    public String getMessage(){
        return this.message;
    }
}

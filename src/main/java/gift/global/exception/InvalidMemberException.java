package gift.global.exception;

public class InvalidMemberException extends RuntimeException{
    private final ErrorCode errorCode;

    public InvalidMemberException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode(){
        return this.errorCode;
    }
}

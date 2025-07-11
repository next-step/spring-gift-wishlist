package gift.exception;

public class InvalidTokenException extends UnAuthorizedException {
    public InvalidTokenException(String message) {
        super(message);
    }
}

package gift.exception;

public class InvalidAuthorizationHeaderException extends RuntimeException {

    public InvalidAuthorizationHeaderException(String message) {
        super(message);
    }

}

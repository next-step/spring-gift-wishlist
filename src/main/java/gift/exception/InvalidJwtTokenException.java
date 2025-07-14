package gift.exception;

public class InvalidJwtTokenException extends AuthenticationException {

    public InvalidJwtTokenException(String message) {
        super(message);
    }
}

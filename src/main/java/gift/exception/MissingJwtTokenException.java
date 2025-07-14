package gift.exception;

public class MissingJwtTokenException extends AuthenticationException {
    public MissingJwtTokenException(String message) {
        super(message);
    }
}

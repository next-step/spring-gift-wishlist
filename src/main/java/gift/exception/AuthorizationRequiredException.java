package gift.exception;

public class AuthorizationRequiredException extends RuntimeException {

    public AuthorizationRequiredException(String message) {
        super(message);
    }
}

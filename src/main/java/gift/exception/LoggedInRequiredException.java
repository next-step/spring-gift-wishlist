package gift.exception;

public class LoggedInRequiredException extends RuntimeException {

    public LoggedInRequiredException(String message) {
        super(message);
    }
}

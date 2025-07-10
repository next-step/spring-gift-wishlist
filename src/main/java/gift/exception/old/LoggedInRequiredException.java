package gift.exception.old;

public class LoggedInRequiredException extends RuntimeException {

    public LoggedInRequiredException(String message) {
        super(message);
    }
}

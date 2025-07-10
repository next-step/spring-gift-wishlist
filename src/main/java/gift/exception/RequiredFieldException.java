package gift.exception;

public class RequiredFieldException extends RuntimeException {

    public RequiredFieldException(String message) {
        super(message);
    }
}

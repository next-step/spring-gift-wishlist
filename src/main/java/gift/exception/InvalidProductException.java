package gift.exception;

public class InvalidProductException extends RuntimeException {

    private String viewName;

    public InvalidProductException(String message) {
        super(message);
    }

    public InvalidProductException(String message, String viewName) {
        super(message);
        this.viewName = viewName;
    }

    public String getViewName() {
        return this.viewName;
    }
}

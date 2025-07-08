package gift.exception;

public class InvalidMemberException extends RuntimeException {
    private String viewName;
    private String attributeName;

    public InvalidMemberException(String message) {
        super(message);
    }

    public InvalidMemberException(String message, String viewName, String attributeName) {
        super(message);
        this.viewName = viewName;
        this.attributeName = attributeName;
    }

    public String getViewName() {
        return this.viewName;
    }

    public String getAttributeName() {
        return attributeName;
    }
}

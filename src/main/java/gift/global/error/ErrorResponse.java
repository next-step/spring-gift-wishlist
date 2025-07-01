package gift.global.error;


import java.util.List;

public class ErrorResponse {

    private List<FieldErrorResponse> fieldErrors;
    private List<ObjectErrorResponse> objectErrors;

    public ErrorResponse(List<FieldErrorResponse> fieldErrors, List<ObjectErrorResponse> objectErrors) {
        this.fieldErrors = fieldErrors;
        this.objectErrors = objectErrors;
    }

    public List<ObjectErrorResponse> getObjectErrors() {
        return objectErrors;
    }

    public List<FieldErrorResponse> getFieldErrors() {
        return fieldErrors;
    }
}

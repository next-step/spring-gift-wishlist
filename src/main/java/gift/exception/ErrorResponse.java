package gift.exception;

import java.util.List;

public record ErrorResponse(String message, List<CustomFieldError> errors) {
}

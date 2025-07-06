package gift.exception.handler;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        String code,
        String message,
        List<FieldErrorDetail> fieldErrors,
        LocalDateTime timestamp
) {

}

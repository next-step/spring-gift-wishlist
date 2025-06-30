package gift.dto.common;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, String message,
                            String trace) {

}

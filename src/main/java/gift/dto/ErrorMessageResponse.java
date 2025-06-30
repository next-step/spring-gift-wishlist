package gift.dto;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record ErrorMessageResponse (
    LocalDateTime timestamp,
    String message,
    int status,
    String error,
    String path,
    String stackTrace
) {
    public static ErrorMessageResponse generateFrom(
        HttpServletRequest request,
        Exception exception,
        HttpStatus status
    ) {
        String stackTrace = "";

        if (exception.getStackTrace() != null && exception.getStackTrace().length > 0) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement element : exception.getStackTrace()) {
                sb.append(element.toString()).append("\n");
            }
            stackTrace = sb.toString();
        }

        return new ErrorMessageResponse(
            LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()),
            exception.getMessage(),
            status.value(),
            status.getReasonPhrase(),
            request.getRequestURI(),
            stackTrace
        );
    }
}
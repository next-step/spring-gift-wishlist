package gift.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleProductNotFoundException(ProductNotFoundException e) {
        ExceptionResponseDto exception = new ExceptionResponseDto(e.getMessage(), 404, LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
    }
}



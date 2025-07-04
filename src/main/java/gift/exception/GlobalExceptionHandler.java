package gift.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<?> handleInvalidProductException(InvalidProductException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDataAccessException(EmptyResultDataAccessException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ErrorCode.NOT_EXISTS_PRODUCT, ErrorCode.NOT_EXISTS_PRODUCT.getErrorMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = "invalid in "+ ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(e.getMessage());
    }

}

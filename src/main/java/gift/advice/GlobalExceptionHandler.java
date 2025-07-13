package gift.advice;

import gift.product.exception.ProductNotFoundException;
import gift.wish.exception.WishOwnerException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(ProductNotFoundException ex){
        return makeErrorResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((err) -> {
            String fieldName = ((FieldError) err).getField();
            String errorMsg = err.getDefaultMessage();
            errors.put(fieldName, errorMsg);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return makeErrorResponseEntity(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(NoSuchElementException ex) {
        return makeErrorResponseEntity(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WishOwnerException.class)
    public ResponseEntity<Map<String, String>> handleWishOwnerException(WishOwnerException ex) {
        return makeErrorResponseEntity(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, String>> handleDataAccessException(DataAccessException ex) {
        return makeErrorResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    ResponseEntity<Map<String, String>> makeErrorResponseEntity(Exception ex, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(Map.of("error", ex.getMessage()));
    }

}

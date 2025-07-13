package gift.exception.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>>
    handlerValidationError(ProductNotFoundException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }
}

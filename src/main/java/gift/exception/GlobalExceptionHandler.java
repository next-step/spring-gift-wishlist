package gift.exception;

import gift.controller.ProductController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice(assignableTypes = ProductController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotExistException.class)
    public ResponseEntity<ErrorResponse> handleProductNotExist(ProductNotExistException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();

            errors.computeIfAbsent(fieldName, key -> new ArrayList<>()).add(errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    public record ErrorResponse(String message) {}

}
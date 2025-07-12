package gift.exception.wishList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class WishExceptionHandler {

    @ExceptionHandler(AlreadyInWishListException.class)
    public ResponseEntity<Map<String, String>>
    handlerValidationError(AlreadyInWishListException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

    @ExceptionHandler(WishAccessDeniedException.class)
    public ResponseEntity<Map<String, String>>
    handlerValidationError(WishAccessDeniedException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errors);
    }

    @ExceptionHandler(WishNotFoundException.class)
    public ResponseEntity<Map<String, String>>
    handlerValidationError(WishNotFoundException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }
}

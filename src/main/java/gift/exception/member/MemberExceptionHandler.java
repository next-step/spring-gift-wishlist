package gift.exception.member;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>>
    handlerValidationError(InvalidCredentialsException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }

    @ExceptionHandler(MemberAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>>
    handlerValidationError(MemberAlreadyExistsException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }
}

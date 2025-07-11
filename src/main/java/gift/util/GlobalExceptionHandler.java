package gift.util;

import gift.exception.DuplicatedEmailException;
import gift.exception.InvalidProductNameException;
import gift.exception.UnAuthenticatedException;
import gift.exception.UnAuthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
            .getAllErrors()
            .forEach((error) -> {
                if (error instanceof FieldError fe) {
                    errors.put(fe.getField(), fe.getDefaultMessage());
                }
            });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonParsingException(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getReason());
        return new ResponseEntity<>(errors, ex.getStatusCode());
    }

    @ExceptionHandler(InvalidProductNameException.class)
    public ResponseEntity<Map<String, String>> handleInvalidProductName(InvalidProductNameException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllUnexpectedException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnAuthenticatedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedException(UnAuthenticatedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.add("WWW-Authenticate", "Bearer");
        return new ResponseEntity<>(error, headers, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedException(UnAuthorizedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(error, headers, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<Map<String, String>> handleDuplicatedEmailException(DuplicatedEmailException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(error, headers, HttpStatus.CONFLICT);
    }
}
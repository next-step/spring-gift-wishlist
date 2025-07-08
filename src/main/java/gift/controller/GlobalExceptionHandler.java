package gift.controller;

import gift.exception.MemberExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorMessages = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessages);
    }

    @ExceptionHandler(MemberExceptions.EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailAlreadyExistsException(MemberExceptions.EmailAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(MemberExceptions.InvalidPasswordException.class)
    public ResponseEntity<?> handleInvalidPasswordException(MemberExceptions.InvalidPasswordException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }

    @ExceptionHandler(MemberExceptions.MemberNotFoundException.class)
    public ResponseEntity<?> handleMemberNotFoundException(MemberExceptions.MemberNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(MemberExceptions.InvalidAuthorizationHeaderException.class)
    public ResponseEntity<?> handleInvalidAuthorizationHeaderException(MemberExceptions.InvalidAuthorizationHeaderException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .header("WWW-Authenticate", "Bearer")
                .body(e.getMessage());
    }

    @ExceptionHandler(MemberExceptions.InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidTokenException(MemberExceptions.InvalidTokenException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .header("WWW-Authenticate", "Bearer")
                .body(e.getMessage());
    }
}

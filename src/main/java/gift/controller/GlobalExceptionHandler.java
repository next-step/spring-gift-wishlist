package gift.controller;

import gift.exception.MemberExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("알 수 없는 오류가 발생했습니다.");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
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

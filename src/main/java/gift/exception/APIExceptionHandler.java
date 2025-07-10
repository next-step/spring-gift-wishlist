package gift.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> productRequestValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach(o -> errors.put(((FieldError) o).getField(), o.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<String> reponseStatusExceptionHandler(ResponseStatusException e){
//        HttpStatusCode httpStatusCode = e.getStatusCode();
//        return ResponseEntity.status(httpStatusCode).body(e.getReason());
//    }
//
//    @ExceptionHandler(LoggedInRequiredException.class)
//    public ResponseEntity<String> LoggedInRequired(LoggedInRequiredException e){
//        return ResponseEntity.badRequest().body(e.getMessage());
//    }

    @ExceptionHandler(MyException.class)
    public ResponseEntity<String> myExceptionHandler(MyException e){
        return ResponseEntity.status(e.getErrorCode().getStatusCode()).body(e.getErrorCode().getMessage());
    }

}

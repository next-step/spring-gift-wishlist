package gift.exception;

import gift.util.BindingResultUtil;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException e) {
        String message = e.getMessage();
        log.trace(message);
        return ResponseEntity.badRequest()
                .body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e) {
        String message = BindingResultUtil.getErrorMessage(e.getBindingResult());
        log.trace(message);
        return ResponseEntity.badRequest()
                .body(message);
    }

    @ExceptionHandler(NotFoundByIdException.class)
    public ResponseEntity<String> handleNotFoundByIdException(NotFoundByIdException e) {
        log.trace(e.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Not Found by ID");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParseError(HttpMessageNotReadableException e) {
        log.trace(e.getMessage());
        return ResponseEntity.badRequest().body("Invalid Request");
    }
}

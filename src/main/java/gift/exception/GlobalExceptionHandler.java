package gift.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        List<CustomFieldError> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new CustomFieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        ErrorResponse errorResponse = new ErrorResponse("입력 값에 대한 유효성 검사에 실패했습니다.", fieldErrors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<SingleErrorResponse> handleIllegalArgumentExceptions(IllegalArgumentException e) {
        log.warn("비즈니스 로직 유효성 검사 실패: {}", e.getMessage());
        SingleErrorResponse errorResponse = new SingleErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SingleErrorResponse> handleExceptions(Exception e) {
        log.error("서버 내부에서 처리되지 않은 예외가 발생했습니다.", e);
        SingleErrorResponse errorResponse = new SingleErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

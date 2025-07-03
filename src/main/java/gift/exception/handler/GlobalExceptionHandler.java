package gift.exception.handler;

import gift.exception.badrequest.CheckMdOkException;
import gift.exception.badrequest.FillAllInfoException;
import gift.exception.badrequest.FillSomeInfoException;
import gift.exception.common.HttpException;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //Custom 예외 처리
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<String> handleCustomException(HttpException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
    
    //Validation 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage) // @NotBlank(message = "...") 등의 message
            .collect(Collectors.joining("\n")); // 여러 메시지가 있을 경우 \n로 연결
        
        return ResponseEntity.badRequest().body(errorMessage);
    }
}

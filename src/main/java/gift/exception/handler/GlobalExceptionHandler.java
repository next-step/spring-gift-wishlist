package gift.exception.handler;

import gift.exception.custom.CheckMdOkException;
import gift.exception.custom.FillAllInfoException;
import gift.exception.custom.FillSomeInfoException;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = CheckMdOkException.class)
    public ResponseEntity<String> handleCheckMdOkException(CheckMdOkException ex) {
        return new ResponseEntity<>("MD와의 협의 후 사용 가능한 이름입니다.", HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(value = FillAllInfoException.class)
    public ResponseEntity<String> handleFillAllInfoException(FillAllInfoException ex) {
        return new ResponseEntity<>("모든 정보가 있어야 수정이 가능합니다.", HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(value = FillSomeInfoException.class)
    public ResponseEntity<String> handleFillSomeInfoException(FillSomeInfoException ex) {
        return new ResponseEntity<>("한 가지 이상의 정보는 있어야 수정이 가능합니다.", HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage) // @NotBlank(message = "...") 등의 message
            .collect(Collectors.joining("\n")); // 여러 메시지가 있을 경우 , 로 연결
        
        return ResponseEntity.badRequest().body(errorMessage);
    }
}

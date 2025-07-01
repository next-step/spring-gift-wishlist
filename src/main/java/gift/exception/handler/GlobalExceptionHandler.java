package gift.exception.handler;

import gift.exception.custom.CheckMdOkException;
import gift.exception.custom.FillAllInfoException;
import gift.exception.custom.FillSomeInfoException;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}

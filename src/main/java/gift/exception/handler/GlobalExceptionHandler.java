package gift.exception.handler;

import gift.exception.custom.CheckMdOkException;
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
}

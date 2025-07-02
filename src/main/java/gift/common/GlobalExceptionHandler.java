package gift.common;

import java.util.stream.Collectors;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex
    ) {
        return new ResponseEntity<>(
            new ErrorResult(
                HttpStatus.BAD_REQUEST,
                ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(MessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(" "))
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResult> handleHttpMessageNotReadableException() {
        return new ResponseEntity<>(
            new ErrorResult(
                HttpStatus.BAD_REQUEST,
                "유효한 값을 입력해주세요. 상품 가격의 경우 최대 2,147,483,647원까지 가능합니다."
            ),
            HttpStatus.BAD_REQUEST
        );
    }
}

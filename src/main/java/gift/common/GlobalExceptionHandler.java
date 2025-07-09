package gift.common;

import gift.common.exceptions.JwtValidationException;
import gift.common.exceptions.LogInFailedException;
import gift.common.exceptions.MemberAlreadyExistsException;
import java.util.stream.Collectors;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.dao.EmptyResultDataAccessException;
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorResult> handleEmptyResultDataAccessException(
        EmptyResultDataAccessException ex
    ) {
        return new ResponseEntity<>(
            new ErrorResult(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
            ),
            HttpStatus.NOT_FOUND
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = JwtValidationException.class)
    public ResponseEntity<ErrorResult> handleJwtValidationException(
        JwtValidationException ex
    ) {
        return new ResponseEntity<>(
            new ErrorResult(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage()
            ),
            HttpStatus.UNAUTHORIZED
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = LogInFailedException.class)
    public ResponseEntity<ErrorResult> handlePasswordMismatchException(
        LogInFailedException ex
    ) {
        return new ResponseEntity<>(
            new ErrorResult(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage()
            ),
            HttpStatus.UNAUTHORIZED
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = MemberAlreadyExistsException.class)
    public ResponseEntity<ErrorResult> handleMemberAlreadyExistsException(
        MemberAlreadyExistsException ex
    ) {
        return new ResponseEntity<>(
            new ErrorResult(
                HttpStatus.CONFLICT,
                ex.getMessage()
            ),
            HttpStatus.CONFLICT
        );
    }
}

package gift.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

// 예외 관리 핸들러
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 자체 예외 및 queryForObject 예외
    @ExceptionHandler({ProductNotFoundException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(RuntimeException e) {
        return e.getMessage();
    }

    // queryForObject로 2개 이상의 제품을 찾은 경우 예외
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleInternalServerError(RuntimeException e) { return e.getMessage(); }

    // MD가 아닌데 특정 문구를 포함한 제품명을 입력한 경우
    @ExceptionHandler(InvalidProductNameException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleForbidden(RuntimeException e) { return e.getMessage(); }

    // ProductRequestDto에 지정된 Validation 검사에서 예외가 발생한 경우
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(MethodArgumentNotValidException e) { return e.getBindingResult().getAllErrors().getFirst().getDefaultMessage(); }
}

package gift.exception.handler;

import gift.exception.ProductHiddenException;
import gift.exception.ProductNotFoundExection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldErrorDetail> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorDetail(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue(),
                        error.getCode()
                ))
                .toList();

        return new ErrorResponse(
                "VALIDATION_FAILED",
                "입력값 검증에 실패했습니다.",
                fieldErrors,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ProductHiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleProductHidden(ProductHiddenException ex) {
        return new ErrorResponse(
                "FORBIDDEN",
                ex.getMessage(),
                List.of(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ProductNotFoundExection.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFoundExeption(ProductNotFoundExection ex) {
        return new ErrorResponse(
                "FORBIDDEN",
                ex.getMessage(),
                List.of(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(IllegalArgumentException ex) {
        return new ErrorResponse(
                "BAD_REQUEST",
                ex.getMessage(),
                List.of(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSQLException(SQLException ex) {
        return new ErrorResponse(
                "DATABASE_ERROR",
                ex.getMessage(),
                List.of(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneral(Exception ex) {
        return new ErrorResponse(
                "INTERNAL_ERROR",
                ex.getMessage(),
                List.of(),
                LocalDateTime.now()
        );
    }
}

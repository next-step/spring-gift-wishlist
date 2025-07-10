package gift.common.exception;

import gift.common.security.exception.InvalidTokenException;
import gift.common.security.exception.MissingTokenException;
import gift.item.exception.ItemNotFoundException;
import gift.member.exception.DuplicateEmailException;
import gift.member.exception.InvalidLoginException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleItemNotFoundException(
        ItemNotFoundException e,
        HttpServletRequest request
    ) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            HttpStatus.NOT_FOUND,
            e.getMessage(),
            URI.create(request.getRequestURI())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e,
        HttpServletRequest request
    ) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            HttpStatus.BAD_REQUEST,
            e.getBindingResult().getFieldError().getDefaultMessage(),
            URI.create(request.getRequestURI())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateEmailException(
        DuplicateEmailException e,
        HttpServletRequest request
    ) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            HttpStatus.CONFLICT,
            e.getMessage(),
            URI.create(request.getRequestURI())
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }


    @ExceptionHandler({
        InvalidTokenException.class,
        MissingTokenException.class,
        InvalidLoginException.class
    })
    public ResponseEntity<ErrorResponseDto> handleUnauthorizedExceptions(
        RuntimeException e,
        HttpServletRequest request
    ) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            HttpStatus.UNAUTHORIZED,
            e.getMessage(),
            URI.create(request.getRequestURI())
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(
        Exception e,
        HttpServletRequest request
    ) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "서버 내부 오류가 발생했습니다.",
            URI.create(request.getRequestURI())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}

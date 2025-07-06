package gift.common.aop;

import gift.dto.error.ErrorMessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import javax.naming.AuthenticationException;
import gift.common.exception.AccessDeniedException;
import java.util.NoSuchElementException;

@RestControllerAdvice(basePackages = "gift.controller.api")
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ProblemDetail> handleAuthenticationException(
            AuthenticationException e, HttpServletRequest request
    ) {
        var errorMessage = new ErrorMessageResponse.Builder(request, e, HttpStatus.UNAUTHORIZED)
                .build();
        return new ResponseEntity<>(errorMessage.toProblemDetail(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(
            AccessDeniedException e, HttpServletRequest request
    ) {
        var errorMessage = new ErrorMessageResponse.Builder(request, e, HttpStatus.FORBIDDEN)
                .build();
        return new ResponseEntity<>(errorMessage.toProblemDetail(), HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(
            NoSuchElementException e, HttpServletRequest request
    ) {
        var errorMessage = new ErrorMessageResponse.Builder(request, e, HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(errorMessage.toProblemDetail(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(
            IllegalArgumentException e, HttpServletRequest request
    ) {
        var errorMessage = new ErrorMessageResponse.Builder(request, e, HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(errorMessage.toProblemDetail(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ProblemDetail> handleEmptyResultDataAccessException(
            EmptyResultDataAccessException e, HttpServletRequest request
    ) {
        var errorMessage = new ErrorMessageResponse.Builder(request, e, HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(errorMessage.toProblemDetail(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ProblemDetail> handleHandlerMethodValidationException(
            HandlerMethodValidationException e,
            HttpServletRequest request
    ) {
        var errorMessage = new ErrorMessageResponse.Builder("유효성 검사에서 오류가 발생했습니다.", HttpStatus.BAD_REQUEST)
                .path(request.getRequestURI())
                .extractValidationErrorsFrom(e)
                .build();
        return new ResponseEntity<>(errorMessage.toProblemDetail(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest request
    ) {
        var errorMessage = new ErrorMessageResponse.Builder("유효성 검사에서 오류가 발생했습니다.", HttpStatus.BAD_REQUEST)
                .path(request.getRequestURI())
                .extractValidationErrorsFrom(e)
                .build();
        return new ResponseEntity<>(errorMessage.toProblemDetail(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(
          ConstraintViolationException e, HttpServletRequest request
    ) {
      var errorMessage = new ErrorMessageResponse.Builder("유효성 검사에서 오류가 발생했습니다.", HttpStatus.BAD_REQUEST)
              .path(request.getRequestURI())
              .extractValidationErrorsFrom(e)
              .build();
      return new ResponseEntity<>(errorMessage.toProblemDetail(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleDefaultException(
            Exception e, HttpServletRequest request
    ) {
        var errorMessage = new ErrorMessageResponse.Builder(request, e, HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return new ResponseEntity<>(errorMessage.toProblemDetail(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

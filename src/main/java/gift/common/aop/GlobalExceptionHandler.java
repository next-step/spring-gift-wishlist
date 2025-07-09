package gift.common.aop;

import gift.common.exception.CriticalServerException;
import gift.common.exception.UnauthorizedException;
import gift.common.model.error.ErrorMessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import gift.common.exception.AccessDeniedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.NoSuchElementException;

@RestControllerAdvice(basePackages = "gift.controller.api")
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private Boolean showStackTrace = false;

    public GlobalExceptionHandler(@Value("${server.error.include-stacktrace}")String stacktraceSetting) {
        if (stacktraceSetting != null && stacktraceSetting.equalsIgnoreCase("always")) {
            this.showStackTrace = true;
        }
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(
            IllegalArgumentException e, HttpServletRequest request
    ) {
        var builder = new ErrorMessageResponse.Builder(request, e, HttpStatus.BAD_REQUEST);
        if (showStackTrace) {
            builder.showStackTrace();
        }
        return new ResponseEntity<>(builder.build().toProblemDetail(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ProblemDetail> handleHandlerMethodValidationException(
            HandlerMethodValidationException e,
            HttpServletRequest request
    ) {
        var builder = new ErrorMessageResponse.Builder("유효성 검사에서 오류가 발생했습니다.", HttpStatus.BAD_REQUEST)
                .path(request.getRequestURI())
                .extractValidationErrorsFrom(e);
        if (showStackTrace) {
            builder.showStackTrace();
        }
        return new ResponseEntity<>(builder.build().toProblemDetail(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest request
    ) {
        var builder = new ErrorMessageResponse.Builder("유효성 검사에서 오류가 발생했습니다.", HttpStatus.BAD_REQUEST)
                .path(request.getRequestURI())
                .extractValidationErrorsFrom(e);
        if (showStackTrace) {
            builder.showStackTrace();
        }
        return new ResponseEntity<>(builder.build().toProblemDetail(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(
            ConstraintViolationException e, HttpServletRequest request
    ) {
        var builder = new ErrorMessageResponse.Builder("유효성 검사에서 오류가 발생했습니다.", HttpStatus.BAD_REQUEST)
                .path(request.getRequestURI())
                .extractValidationErrorsFrom(e);
        if (showStackTrace) {
            builder.showStackTrace();
        }
        return new ResponseEntity<>(builder.build().toProblemDetail(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ProblemDetail> handleAuthenticationException(
            UnauthorizedException e, HttpServletRequest request
    ) {
        var builder = new ErrorMessageResponse.Builder(request, e, HttpStatus.UNAUTHORIZED);
        if (showStackTrace) {
            builder.showStackTrace();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("WWW-Authenticate", "Bearer")
                .body(builder.build().toProblemDetail());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(
            AccessDeniedException e,
            HttpServletRequest request
    ) {
        var builder = new ErrorMessageResponse.Builder(request, e, HttpStatus.FORBIDDEN);
        if (showStackTrace) {
            builder.showStackTrace();
        }
        return new ResponseEntity<>(builder.build().toProblemDetail(), HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(
            NoSuchElementException e, HttpServletRequest request
    ) {
        var builder = new ErrorMessageResponse.Builder(request, e, HttpStatus.NOT_FOUND);
        if (showStackTrace) {
            builder.showStackTrace();
        }
        return new ResponseEntity<>(builder.build().toProblemDetail(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ProblemDetail> handleEmptyResultDataAccessException(
            EmptyResultDataAccessException e, HttpServletRequest request
    ) {
        var builder = new ErrorMessageResponse.Builder(request, e, HttpStatus.NOT_FOUND);
        if (showStackTrace) {
            builder.showStackTrace();
        }
        return new ResponseEntity<>(builder.build().toProblemDetail(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoResourceFoundException(
            NoResourceFoundException e, HttpServletRequest request
    ) {
        var builder = new ErrorMessageResponse.Builder(request, e, HttpStatus.NOT_FOUND);
        if (showStackTrace) {
            builder.showStackTrace();
        }
        ProblemDetail errorDetail = builder.build().toProblemDetail();
        errorDetail.setDetail("요청한 리소스를 찾을 수 없습니다.");
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateKeyException(
            DuplicateKeyException e, HttpServletRequest request
    ) {
        var builder = new ErrorMessageResponse.Builder(request, e, HttpStatus.CONFLICT);
        if (showStackTrace) {
            builder.showStackTrace();
        }
        return new ResponseEntity<>(builder.build().toProblemDetail(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CriticalServerException.class)
    public ResponseEntity<ProblemDetail> handleCriticalServerException(
            CriticalServerException e, HttpServletRequest request
    ) {
        var builder = new ErrorMessageResponse.Builder(request, e, HttpStatus.INTERNAL_SERVER_ERROR);
        if (showStackTrace) {
            builder.showStackTrace();
        }
        log.error("치명적인 서버 오류가 발생했습니다: {}", e.getMessage(), e);
        return new ResponseEntity<>(builder.build().toProblemDetail(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleDefaultException(
            Exception e, HttpServletRequest request
    ) {
        var builder = new ErrorMessageResponse.Builder(request, e, HttpStatus.INTERNAL_SERVER_ERROR);
        if (showStackTrace) {
            builder.showStackTrace();
        }
        log.error("예상치 못한 오류가 발생했습니다: {}", e.getMessage(), e);
        return new ResponseEntity<>(builder.build().toProblemDetail(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

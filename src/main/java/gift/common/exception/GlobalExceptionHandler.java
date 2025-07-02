package gift.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Order
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ErrorResponse handleBind(BindException ex, HttpServletRequest req) {
        List<ProblemDetail> fieldErrors = ex.getFieldErrors().stream()
                .map(fe -> {
                    ErrorCode errorCode = determineErrorCodeFromMessage(fe.getDefaultMessage());
                    ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                        errorCode.getHttpStatus(), 
                        errorCode.getMessage()
                    );
                    pd.setProperty("code", errorCode.name());
                    pd.setProperty("field", fe.getField());
                    pd.setProperty("rejectedValue", fe.getRejectedValue());
                    return pd;
                })
                .toList();

        ErrorCode validationError = ErrorCode.VALIDATION_FAILED;
        return ErrorResponse.builder(ex, validationError.getHttpStatus(), validationError.getMessage())
                .property("code", validationError.name())
                .property("errors", fieldErrors)
                .property("path", req.getRequestURI())
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<ProblemDetail> fieldErrors = ex.getFieldErrors().stream()
                .map(fe -> {
                    ErrorCode errorCode = determineErrorCodeFromMessage(fe.getDefaultMessage());
                    ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                            errorCode.getHttpStatus(),
                            errorCode.getMessage()
                    );
                    pd.setProperty("code", errorCode.name());
                    pd.setProperty("field", fe.getField());
                    pd.setProperty("rejectedValue", fe.getRejectedValue());
                    return pd;
                })
                .toList();

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        ErrorResponse err = ErrorResponse.builder(ex, errorCode.getHttpStatus(), errorCode.getMessage())
                .property("code", errorCode.name())
                .property("errors", fieldErrors)
                .property("path", request.getDescription(false).replace("uri=", ""))
                .build();

        ProblemDetail body = err.updateAndGetBody(getMessageSource(), LocaleContextHolder.getLocale());
        return handleExceptionInternal(ex, body, headers, errorCode.getHttpStatus(), request);
    }

    @ExceptionHandler(NullPointerException.class)
    public ErrorResponse handleNpe(NullPointerException ex, HttpServletRequest req) {
        ErrorCode errorCode = ErrorCode.REQUIRED_VALUE_MISSING;
        return ErrorResponse.builder(ex, errorCode.getHttpStatus(), errorCode.getMessage())
                .property("code", errorCode.name())
                .property("path", req.getRequestURI())
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArg(IllegalArgumentException ex, HttpServletRequest req) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        return ErrorResponse.builder(ex, errorCode.getHttpStatus(), errorCode.getMessage())
                .property("code", errorCode.name())
                .property("path", req.getRequestURI())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleUnexpected(Exception ex, HttpServletRequest req) {
        ErrorCode errorCode = ErrorCode.UNEXPECTED_ERROR;
        return ErrorResponse.builder(ex, errorCode.getHttpStatus(), errorCode.getMessage())
                .property("code", errorCode.name())
                .property("path", req.getRequestURI())
                .build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse handleNoSuchElement(NoSuchElementException ex, HttpServletRequest req) {
        ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;
        return ErrorResponse.builder(ex, errorCode.getHttpStatus(), errorCode.getMessage())
                .property("code", errorCode.name())
                .property("path", req.getRequestURI())
                .build();
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponse handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        String detail = String.format("'%s' 파라미터 값 '%s'는 올바른 타입이 아닙니다.",
                ex.getName(), ex.getValue());
        return ErrorResponse.builder(ex, errorCode.getHttpStatus(), detail)
                .property("code", errorCode.name())
                .property("field", ex.getName())
                .property("rejectedValue", ex.getValue())
                .property("path", req.getRequestURI())
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ErrorCode errorCode = ErrorCode.MISSING_REQUIRED_PARAMETER;
        ErrorResponse err = ErrorResponse.builder(ex, errorCode.getHttpStatus(), errorCode.getMessage())
                .property("code", errorCode.name())
                .property("parameter", ex.getParameterName())
                .build();

        ProblemDetail body = err.updateAndGetBody(getMessageSource(), LocaleContextHolder.getLocale());
        return handleExceptionInternal(ex, body, headers, errorCode.getHttpStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_FORMAT;
        ErrorResponse err = ErrorResponse.builder(ex, errorCode.getHttpStatus(), errorCode.getMessage())
                .property("code", errorCode.name())
                .build();

        ProblemDetail body = err.updateAndGetBody(getMessageSource(), LocaleContextHolder.getLocale());
        return handleExceptionInternal(ex, body, headers, errorCode.getHttpStatus(), request);
    }

    //TODO : 현재 메세지 기반으로 에러코드를 추출하는데 해결책을 찾고 리팩토링이 필요함
    private ErrorCode determineErrorCodeFromMessage(String message) {
        if (message == null) {
            return ErrorCode.VALIDATION_FAILED;
        }
        
        if (message.contains("최대 길이를 초과")) {
            return ErrorCode.PRODUCT_NAME_TOO_LONG;
        }
        if (message.contains("허용되지 않은 특수문자")) {
            return ErrorCode.PRODUCT_NAME_INVALID_CHARS;
        }
        if (message.contains("카카오")) {
            return ErrorCode.PRODUCT_NAME_KAKAO_RESTRICTED;
        }
        
        if (message.contains("1원 이상")) {
            return ErrorCode.PRODUCT_PRICE_INVALID;
        }
        if (message.contains("가격은 필수")) {
            return ErrorCode.PRODUCT_PRICE_REQUIRED;
        }
        
        if (message.contains("공백입니다") || message.contains("필수 입력값")) {
            return ErrorCode.REQUIRED_VALUE_MISSING;
        }
        
        return ErrorCode.VALIDATION_FAILED;
    }
}

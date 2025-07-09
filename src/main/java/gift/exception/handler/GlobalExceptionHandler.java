package gift.exception.handler;

import gift.exception.InvalidAuthExeption;
import gift.exception.InvalidBearerAuthException;
import gift.exception.MemberNotFoundException;
import gift.exception.ProductHiddenException;
import gift.exception.ProductNotFoundExection;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Object handleValidationExceptions(
            Exception ex,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {

        response.setStatus(HttpStatus.BAD_REQUEST.value());

        if (request.getHeader("Accept") != null
                && request.getHeader("Accept").contains("application/json")) {

            MethodArgumentNotValidException manv =
                    (MethodArgumentNotValidException) (ex instanceof MethodArgumentNotValidException
                            ? ex : null);

            List<FieldErrorDetail> fieldErrors = manv.getBindingResult()
                    .getFieldErrors().stream()
                    .map(error -> new FieldErrorDetail(
                            error.getField(),
                            error.getDefaultMessage(),
                            error.getRejectedValue(),
                            error.getCode()))
                    .toList();

            return new ErrorResponse(
                    ErrorCode.VALIDATION_FAILED,
                    "입력값 검증에 실패했습니다.",
                    fieldErrors,
                    LocalDateTime.now());
        }

        BindingResult br = ex instanceof MethodArgumentNotValidException
                ? ((MethodArgumentNotValidException) ex).getBindingResult()
                : ((BindException) ex).getBindingResult();

        model.addAttribute("productForm", br.getTarget());
        model.addAttribute(
                BindingResult.MODEL_KEY_PREFIX + "productForm",
                br
        );

        return "admin/product_form";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException ex) {
        List<FieldErrorDetail> details = ex.getConstraintViolations().stream()
                .map(cv -> new FieldErrorDetail(
                        cv.getPropertyPath().toString(),
                        cv.getMessage(),
                        cv.getInvalidValue(),
                        cv.getConstraintDescriptor()
                                .getAnnotation()
                                .annotationType()
                                .getSimpleName()))
                .toList();

        return new ErrorResponse(
                ErrorCode.VALIDATION_FAILED,
                "데이터 무결성 검사에 실패했습니다.",
                details,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNullPointer(NullPointerException ex) {
        return new ErrorResponse(
                ErrorCode.VALIDATION_FAILED,
                ex.getMessage(),
                List.of(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ProductHiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleProductHiddenExeption(ProductHiddenException ex) {
        return new ErrorResponse(
                ErrorCode.FORBIDDEN,
                ex.getMessage(),
                List.of(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(InvalidAuthExeption.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidAuthExeption(InvalidAuthExeption ex) {
        return new ErrorResponse(
                ErrorCode.UNAUTHORIZED,
                ex.getMessage(),
                List.of(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(InvalidBearerAuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidAuthExeption(InvalidBearerAuthException ex) {
        return new ErrorResponse(
                ErrorCode.UNAUTHORIZED,
                ex.getMessage(),
                List.of(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ProductNotFoundExection.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFoundExeption(ProductNotFoundExection ex) {
        return new ErrorResponse(
                ErrorCode.FORBIDDEN,
                ex.getMessage(),
                List.of(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleMemberNotFoundExeption(MemberNotFoundException ex) {
        return new ErrorResponse(
                ErrorCode.FORBIDDEN,
                ex.getMessage(),
                List.of(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return new ErrorResponse(
                ErrorCode.BAD_REQUEST,
                ex.getMessage(),
                List.of(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentExeption(IllegalArgumentException ex) {
        return new ErrorResponse(
                ErrorCode.BAD_REQUEST,
                ex.getMessage(),
                List.of(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSQLException(SQLException ex) {
        return new ErrorResponse(
                ErrorCode.DATABASE_ERROR,
                ex.getMessage(),
                List.of(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneral(Exception ex) {
        return new ErrorResponse(
                ErrorCode.INTERNAL_ERROR,
                ex.getMessage(),
                List.of(),
                LocalDateTime.now()
        );
    }
}

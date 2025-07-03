package gift.controller;

import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import gift.exception.ApprovalRequiredException;
import gift.exception.ProductCreateException;
import gift.exception.ProductNotFoundException;
import gift.exception.ProductUpdateException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> messages = e.getBindingResult().getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList(); // 오류가 발생하는 항목들을 리스트에 담아 응답

        ProblemDetail error =
            ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "유효성 검사에 실패했습니다.");
        error.setProperty("errors", messages); // `detail`엔 String만 담을 수 있기 때문에, 별도의 필드 사용

        return error;
    }

    @ExceptionHandler(ApprovalRequiredException.class)
    public ProblemDetail handleApprovalRequiredException(ApprovalRequiredException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleProductNotFoundException(ProductNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ProductCreateException.class)
    public ProblemDetail handleProductCreateException(ProductCreateException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(ProductUpdateException.class)
    public ProblemDetail handleProductUpdateException(ProductUpdateException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}

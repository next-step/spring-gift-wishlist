package gift.config;

import gift.common.dto.response.ErrorResponseDto;
import gift.common.exception.CreationFailException;
import gift.common.exception.EntityNotFoundException;
import gift.common.exception.RequestValidateFailException;
import gift.domain.product.ProductDomainRuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RequestValidateFailException.class)
    public ResponseEntity<ErrorResponseDto> handleRequestValidateFail(RequestValidateFailException e) {
        log.warn("RequestValidateFail: {}", e.getMessage());
        var response = new ErrorResponseDto(e.getMessage(), 400);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleEntityNotFound(EntityNotFoundException e) {
        log.warn("EntityNotFoundException: {}", e.getMessage());
        return ResponseEntity.notFound().build(); //404
    }

    @ExceptionHandler(CreationFailException.class)
    public ResponseEntity<ErrorResponseDto> handleCreationFail(CreationFailException e) {
        log.warn("CreationFailException: {}", e.getMessage());
        var response = new ErrorResponseDto(e.getMessage(), 500);
        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(ProductDomainRuleException.class)
    public ResponseEntity<ErrorResponseDto> handleProductDomainRoleException(ProductDomainRuleException e) {
        log.warn("ProductDomainRuleException: {}", e.getMessage());
        var response = new ErrorResponseDto(e.getMessage(), 422);
        return ResponseEntity.unprocessableEntity().body(response);
    }
}

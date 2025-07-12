package gift.config;

import gift.common.dto.response.ErrorResponseDto;
import gift.common.exception.*;
import gift.common.exception.SecurityException;
import gift.domain.product.ProductDomainRuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthorityException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthorityException(AuthorityException e) {
        log.warn("AuthorityException: {}", e.getMessage());
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage(), 403);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(RequestValidateFailException.class)
    public ResponseEntity<ErrorResponseDto> handleRequestValidateFail(RequestValidateFailException e) {
        log.warn("RequestValidateFail: {}", e.getMessage());
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage(), 400);
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
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage(), 500);
        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(ProductDomainRuleException.class)
    public ResponseEntity<ErrorResponseDto> handleProductDomainRoleException(ProductDomainRuleException e) {
        log.warn("ProductDomainRuleException: {}", e.getMessage());
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage(), 422);
        return ResponseEntity.unprocessableEntity().body(response);
    }

    @ExceptionHandler(RegisterFailException.class)
    public ResponseEntity<ErrorResponseDto> handleRegisterFail(RegisterFailException e) {
        log.warn("RegisterFailException: {}", e.getMessage());
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage(), e.getStatus().value());
        return ResponseEntity.status(response.code()).body(response);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponseDto> handleSecurityException(SecurityException e) {
        log.warn("SecurityException: {}", e.getMessage());
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage(), 401);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}

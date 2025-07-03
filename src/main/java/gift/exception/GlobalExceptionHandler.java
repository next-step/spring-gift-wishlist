package gift.exception;

import gift.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Void> handleProductNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(OperationFailedException.class)
    public ResponseEntity<ErrorResponseDto> handleOperationFailed(OperationFailedException ex) {
        ErrorResponseDto responseDto = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                "Operation Failed",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidProduct(InvalidProductException ex) {
        ErrorResponseDto responseDto = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Product",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }
}

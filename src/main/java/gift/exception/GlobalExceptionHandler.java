package gift.exception;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleArgumentNotValidException(
        MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        List<ErrorResponse.ValidationError> validationErrorList = bindingResult.getFieldErrors()
            .stream()
            .map(fieldError -> ErrorResponse.ValidationError.of(
                fieldError.getField(),
                fieldError.getDefaultMessage()
            ))
            .toList();

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, validationErrorList);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedShaAlgorithmException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedShaAlgorithmException(
        UnsupportedShaAlgorithmException exception
    ) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.NO_SUPPORTED_SHA256_ALGORITHM);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
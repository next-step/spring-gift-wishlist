package gift.controller;

import gift.exception.CustomErrorResponse;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(errorCode.getStatus(),
                e.getMessage());
        return new ResponseEntity<>(customErrorResponse, errorCode.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<CustomErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(HttpStatus.BAD_REQUEST,
                e.getBindingResult().getFieldError().getDefaultMessage());
        return new ResponseEntity<>(customErrorResponse, HttpStatus.BAD_REQUEST);
    }
}

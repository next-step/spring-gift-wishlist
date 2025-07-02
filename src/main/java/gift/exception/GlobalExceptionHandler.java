package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public ResponseEntity<CustomErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
    String errorMessage = exception.getBindingResult()
        .getFieldErrors()
        .stream()
        .findFirst()
        .map(fieldError -> fieldError.getDefaultMessage())
        .orElse("잘못된 요청입니다.");
    CustomErrorResponse errorResponse=new CustomErrorResponse(exception.getStatusCode(), errorMessage);
    return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value=NameHasKakaoException.class)
  public ResponseEntity<CustomErrorResponse> hasKakaoExceptionResponseEntity(NameHasKakaoException exception){
    CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
  }



  @ExceptionHandler(value=ProductNotFoundException.class)
  public ResponseEntity<CustomErrorResponse> handleProductNotFoundException(ProductNotFoundException exception){
    CustomErrorResponse errorResponse=new CustomErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
  }
}

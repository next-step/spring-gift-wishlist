package gift.product.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


// 400번대 모든 에러와 500번 에러에 대해 상태 코드와 디버깅 메시지을 전달하는 전역 핸들러
@ControllerAdvice
public class GlobalExceptionHandler {

	public record ErrorResponse(int code, String message) {}

	@ExceptionHandler({ MethodArgumentNotValidException.class, IllegalArgumentException.class })
	public ResponseEntity<ErrorResponse> handleClientErrors(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handlerServerErrors(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
	}
}

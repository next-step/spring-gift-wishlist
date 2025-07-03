package gift.product.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

	public record ErrorResponse(int code, String message) {}

	// @Valid 예외
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

		String debuggingMessage = e.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), debuggingMessage));
	}

	// 카카오 키워드 포함 예외
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
	}


	// 서버 에러
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handlerServerErrors(Exception e) {
		return ResponseEntity.status(HttpStatus
			.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
	}
}

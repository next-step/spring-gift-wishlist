package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {

        // 1. 예외 객체에서 모든 필드 에러 정보를 가져오기
        List<CustomFieldError> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                // 2. 각 FieldError를 우리가 정의한 CustomFieldError 객체로 변환.
                .map(error -> new CustomFieldError(error.getField(), error.getDefaultMessage()))
                // 3. List 형태로 수집.
                .collect(Collectors.toList());

        // 4. 최종 에러 응답 객체를 생성.
        ErrorResponse errorResponse = new ErrorResponse("입력 값에 대한 유효성 검사에 실패했습니다.", fieldErrors);

        // 5. HTTP 400 (Bad Request) 상태 코드와 함께 에러 응답을 반환.
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    public record ErrorResponse(String message, List<CustomFieldError> errors) {}

    public record CustomFieldError(String field, String defaultMessage) {}
}

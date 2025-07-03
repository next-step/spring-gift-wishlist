package gift.exception;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // PathVariable/RequestParam 타입 불일치 시 예외를 처리할 핸들러
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        Map<String, String> errors = new HashMap<>();
        String field = e.getName();             // "id" 같은 파라미터 이름
        String requiredType = e.getRequiredType() != null
            ? e.getRequiredType().getSimpleName()
            : "유효한 타입";
        errors.put("error",
            String.format("'%s' 파라미터는 %s 형식이어야 합니다.", field, requiredType));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        Map<String, String> errors = new HashMap<>();

        String defaultMsg = "요청 JSON 형식이 잘못되었습니다.";
        Throwable cause = e.getCause();

        if (cause instanceof com.fasterxml.jackson.databind.exc.MismatchedInputException mie) {
            // Jackson이 파싱에 실패한 필드 이름 추출
            String field = mie.getPath().stream()
                .map(Reference::getFieldName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("."));
            // 기대 타입 이름
            String targetType = (mie.getTargetType() != null)
                ? mie.getTargetType().getSimpleName()
                : "유효한 타입";
            errors.put("error", String.format("'%s' 필드는 %s 형식이어야 합니다.", field, targetType));
        } else {
            errors.put("error", defaultMsg);
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 유효성 검사 실패 시 예외를 처리할 핸들러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors()
            .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}

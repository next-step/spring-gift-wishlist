package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 @Valid 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, ErrorResponse>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, ErrorResponse> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String message = error.getDefaultMessage();

            ErrorCode errorCode = switch (message) {
                case "상품 이름은 비어 있을 수 없습니다." -> ErrorCode.NAME_BLANK;
                case "상품 이름은 최대 15자까지 입력할 수 있습니다." -> ErrorCode.NAME_TOO_LONG;
                case "상품 이름에는 허용되지 않은 특수문자가 포함되어 있습니다." -> ErrorCode.NAME_INVALID_CHARACTERS;
                case "\"카카오\"가 포함된 상품 이름은 사용할 수 없습니다." -> ErrorCode.NAME_CONTAINS_KAKAO;
                case "가격은 0 이상이어야 합니다." -> ErrorCode.PRICE_NEGATIVE;
                case "이미지 URL은 비어 있을 수 없습니다." -> ErrorCode.IMGURL_BLANK;
                default -> ErrorCode.INTERNAL_SERVER_ERROR;
            };

            errors.put(field, new ErrorResponse(errorCode));
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // 404 상품 미존재 예외
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ErrorCode.PRODUCT_NOT_FOUND));
    }

    // 500 그 외 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}

package gift.exception;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleProductValidationException(
            MethodArgumentNotValidException ex) {

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        pd.setType(URI.create("/errors/invalid-input"));
        pd.setTitle("입력값 검증에 실패했습니다.");

        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                                             .map(error -> {
                                                 Map<String, String> errorMap = new HashMap<>();
                                                 errorMap.put("field", error.getField());
                                                 errorMap.put("message", error.getDefaultMessage());
                                                 return errorMap;
                                             })
                                             .toList();
        pd.setProperty("invalid-input", errors);

        return ResponseEntity.of(pd).build();
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleProductNotFoundException(
            ProductNotFoundException ex) {

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        pd.setType(URI.create("/errors/product-not-found"));
        pd.setTitle("상품을 찾을 수 없습니다.");
        pd.setDetail(ex.getMessage());

        return ResponseEntity.of(pd).build();
    }

}

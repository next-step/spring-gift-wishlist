package gift.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setType(URI.create("localhost:8080/api/products/validation-error"));
    problem.setTitle("Your request parameters didn't validate.");
    problem.setDetail("One or more fields failed validation.");
    problem.setInstance(URI.create(request.getRequestURI()));

    List<Map<String, String>> invalidParams = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> Map.of(
            "name", error.getField(),
            "reason", error.getDefaultMessage()
        ))
        .toList();

    problem.setProperty("invalid-params", invalidParams);

    return ResponseEntity.badRequest().body(problem);
  }

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<ProblemDetail> handleInvalidRequest(InvalidRequestException ex,
      HttpServletRequest request) {
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setType(URI.create("localhost:8080/api/products/invalid-request"));
    problem.setTitle("Invalid request");
    problem.setDetail(ex.getMessage());
    problem.setInstance(URI.create(request.getRequestURI()));

    return ResponseEntity.badRequest().body(problem);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException ex,
      HttpServletRequest request) {
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problem.setType(URI.create("localhost:8080/api/products/product-not-found"));
    problem.setTitle("Product not found");
    problem.setDetail(ex.getMessage());
    problem.setInstance(URI.create(request.getRequestURI()));

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
  }
}

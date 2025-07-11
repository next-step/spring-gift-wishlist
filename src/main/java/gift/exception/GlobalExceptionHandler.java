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

  @ExceptionHandler(EmailAlreadyRegisteredException.class)
  public ResponseEntity<ProblemDetail> handleEmailAlreadyRegistered(EmailAlreadyRegisteredException ex,
      HttpServletRequest request) {
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
    problem.setType(URI.create("localhost:8080/api/members/email-already-registered"));
    problem.setTitle("Email already registered");
    problem.setDetail(ex.getMessage());
    problem.setInstance(URI.create(request.getRequestURI()));

    return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
  }

  @ExceptionHandler(MemberNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleMemberNotFound(MemberNotFoundException ex,
      HttpServletRequest request) {
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problem.setType(URI.create("localhost:8080/api/members/member-not-found"));
    problem.setTitle("Member Not Found");
    problem.setDetail(ex.getMessage());
    problem.setInstance(URI.create(request.getRequestURI()));

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
  }

  @ExceptionHandler(PasswordMismatchException.class)
  public ResponseEntity<ProblemDetail> handlePasswordMismatch(PasswordMismatchException ex,
      HttpServletRequest request) {
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
    problem.setType(URI.create("localhost:8080/api/members/password-mismatch"));
    problem.setTitle("Password Mismatch");
    problem.setDetail(ex.getMessage());
    problem.setInstance(URI.create(request.getRequestURI()));

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleProductNotFound(ProductNotFoundException ex,
      HttpServletRequest request) {
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problem.setType(URI.create("localhost:8080/api/products/product-not-found"));
    problem.setTitle("Product Not Found");
    problem.setDetail(ex.getMessage());
    problem.setInstance(URI.create(request.getRequestURI()));

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
  }

  @ExceptionHandler(WishListItemNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleWishListItemNotFound(WishListItemNotFoundException ex,
      HttpServletRequest request) {
    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problem.setType(URI.create("localhost:8080/api/wishlist/item-not-found"));
    problem.setTitle("WishList Item Not Found");
    problem.setDetail(ex.getMessage());
    problem.setInstance(URI.create(request.getRequestURI()));

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
  }
}

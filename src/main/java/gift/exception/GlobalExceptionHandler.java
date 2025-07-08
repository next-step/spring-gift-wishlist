package gift.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleValidation(ValidationException ex, Model model) {
    model.addAttribute("product", ex.getBindingResult().getTarget());
    model.addAttribute("org.springframework.validation.BindingResult.product", ex.getBindingResult());

    // 폼 재구성 (action/method 등 기본값 지정 필요)
    model.addAttribute("action", "/admin/products");
    model.addAttribute("method", "post");
    return "admin/product-form";
  }

//  @ExceptionHandler(SecurityException.class)
//  @ResponseStatus(HttpStatus.FORBIDDEN)
//  public ResponseEntity<String> handleSecurity(SecurityException ex) {
//    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
//  }
}


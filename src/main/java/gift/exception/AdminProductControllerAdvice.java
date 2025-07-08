package gift.exception;

import gift.controller.AdminProductController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = AdminProductController.class)
public class AdminProductControllerAdvice {

    @ExceptionHandler(InvalidProductException.class)
    public String handleInvalidProductException(
            InvalidProductException e,
            Model model,
            HttpServletRequest request
           ) {
        model.addAttribute("product", request.getAttribute("product"));
        model.addAttribute("globalErrorMessage",e.getMessage());
        return e.getViewName();
    }
}

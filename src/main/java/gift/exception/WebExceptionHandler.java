package gift.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "gift.controller.admin")
public class WebExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public String handleItemNotFoundException(ItemNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(DuplicateItemException.class)
    public String handleDuplicateItemException(DuplicateItemException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/409";
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
        return "error/500";
    }
}
package gift.exception;

import gift.controller.AdminMemberController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = AdminMemberController.class)
public class AdminMemberControllerAdvice {

    @ExceptionHandler(InvalidMemberException.class)
    public String handleInvalidMemberException(
            InvalidMemberException e,
            Model model,
            HttpServletRequest request
    ) {
        model.addAttribute("member", request.getAttribute("member"));
        model.addAttribute(e.getAttributeName(),e.getMessage());
        return e.getViewName();
    }
}

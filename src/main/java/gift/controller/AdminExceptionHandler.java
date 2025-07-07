package gift.controller;

import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import gift.exception.ApprovalRequiredException;
import gift.exception.ProductCreateException;
import gift.exception.ProductNotFoundException;
import gift.exception.ProductUpdateException;
import gift.exception.MemberDeleteException;
import gift.exception.MemberNotFoundException;
import gift.exception.MemberUpdateException;

@ControllerAdvice(
    assignableTypes = {ProductAdminController.class, MemberAdminController.class}
)
public class AdminExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e,
        Model model
    ) {
        List<String> messages = e.getBindingResult().getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList();

        model.addAttribute("errorTitle", "유효성 검사 실패");
        model.addAttribute("errorMessages", messages);
        return "error";
    }

    @ExceptionHandler({
        ProductNotFoundException.class,
        ProductCreateException.class,
        ProductUpdateException.class,
        ApprovalRequiredException.class,
        MemberNotFoundException.class,
        MemberUpdateException.class,
        MemberDeleteException.class
    })
    public String handleProductExceptions(RuntimeException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }
}

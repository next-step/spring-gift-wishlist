package com.example.demo.exception.handler;

import com.example.demo.controller.user.UserPageController;
import com.example.demo.exception.InvalidLoginException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(assignableTypes = {UserPageController.class})
public class PageExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public String handleValidationException(
      MethodArgumentNotValidException ex,
      RedirectAttributes redirectAttributes
  ) {
    String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                            .map(error -> error.getDefaultMessage())
                            .findFirst()
                            .orElse("입력값이 올바르지 않습니다.");

    redirectAttributes.addFlashAttribute("signupError", errorMessage);
    return "redirect:/signup-page";
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public String handleIllegalArgument(
      IllegalArgumentException ex,
      RedirectAttributes redirectAttributes
  ) {
    redirectAttributes.addFlashAttribute("signupError", ex.getMessage());
    return "redirect:/signup-page";
  }

  @ExceptionHandler(InvalidLoginException.class)
  public String handleInvalidLogin(
      InvalidLoginException ex,
      RedirectAttributes redirectAttributes
  ) {
    redirectAttributes.addFlashAttribute("loginError", ex.getMessage());
    return "redirect:/login-page";
  }

}

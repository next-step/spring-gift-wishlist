package gift.exception;

import gift.dto.MemberRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

  // ✅ 이메일/비밀번호 형식 오류 등 바인딩 예외
  @ExceptionHandler(BindException.class)
  public String handleBindException(BindException ex,
      HttpServletRequest request,
      Model model,
      HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    BindingResult bindingResult = ex.getBindingResult();

    model.addAttribute("memberRequestDto", bindingResult.getTarget());
    model.addAttribute("org.springframework.validation.BindingResult.memberRequestDto", bindingResult);

    return "user/register";
  }

  // ✅ 중복 이메일 예외
  @ExceptionHandler(DuplicateEmailException.class)
  public String handleDuplicateEmail(DuplicateEmailException ex,
      HttpServletRequest request,
      Model model,
      HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    MemberRequestDto dto = new MemberRequestDto();
    dto.setEmail(request.getParameter("email"));
    dto.setPassword(request.getParameter("password"));

    model.addAttribute("memberRequestDto", dto);
    model.addAttribute("error", ex.getMessage());

    return "user/register";
  }
}



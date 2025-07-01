package gift.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice //애플리케이션 전역에서 발생하는 예외를 처리하기 위함
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public String handleNotFoundException(ResponseStatusException e, Model model){
        model.addAttribute("errorMsg", e.getReason());
        return "ProductNotFound";
    }
}

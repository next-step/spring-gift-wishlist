package gift.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Order(Ordered.LOWEST_PRECEDENCE)
@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object handleGlobalException(Exception ex, HttpServletRequest request) {
        System.err.println("Unhandled exception occurred: " + ex.getMessage());

        String requestUri = request.getRequestURI();

        if (requestUri.startsWith("/api/")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 내부 오류가 발생했습니다.");
        }

        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("errorMessage", "서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
        return modelAndView;
    }
}
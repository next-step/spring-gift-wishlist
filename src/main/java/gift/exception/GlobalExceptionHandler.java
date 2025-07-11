package gift.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(AuthenticationException.class)
    public Object handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return createErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
        return new ModelAndView("redirect:/members/login");
    }

    @ExceptionHandler(AuthorizationException.class)
    public Object handleAuthorizationException(AuthorizationException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return createErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
        }
        return createErrorModelAndView("error/403", ex.getMessage());
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleLoginException(LoginException ex) {
        return createErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public Object handleItemNotFoundException(ItemNotFoundException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        return createErrorModelAndView("error/404", ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Object handleDataIntegrityViolationException(HttpServletRequest request) {
        String message = "이미 존재하는 이름 또는 이메일입니다. 다른 값을 사용해주세요.";
        if (isApiRequest(request)) {
            return createErrorResponse(HttpStatus.CONFLICT, message);
        }
        return createErrorModelAndView("error/409", message);
    }

    @ExceptionHandler(Exception.class)
    public Object handleGlobalException(Exception ex, HttpServletRequest request) {
        System.err.println("Unhandled exception occurred: " + ex.getClass().getName() + " | " + ex.getMessage());
        String message = "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.";
        if (isApiRequest(request)) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }
        return createErrorModelAndView("error/500", message);
    }

    private boolean isApiRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/");
    }

    private ResponseEntity<String> createErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(message);
    }

    private ModelAndView createErrorModelAndView(String viewName, String message) {
        ModelAndView modelAndView = new ModelAndView(viewName);
        modelAndView.addObject("errorMessage", message);
        return modelAndView;
    }
}
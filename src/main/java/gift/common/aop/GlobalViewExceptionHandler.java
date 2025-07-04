package gift.common.aop;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.ModelAndView;
import java.util.NoSuchElementException;

@ControllerAdvice(basePackages = "gift.controller.view")
public class GlobalViewExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ModelAndView handleNoSuchElementException(
            NoSuchElementException e
    ) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("statusCode", HttpStatus.NOT_FOUND.value());
        modelAndView.addObject("errMessage", "요청한 리소스가 존재하지 않습니다: " + e.getMessage());

        return modelAndView;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(
            IllegalArgumentException e
    ) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("statusCode", HttpStatus.BAD_REQUEST.value());
        modelAndView.addObject("errMessage", "잘못된 요청입니다: " + e.getMessage());

        return modelAndView;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        ModelAndView modelAndView = new ModelAndView("error");
        String validationError = e.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        modelAndView.addObject("statusCode", HttpStatus.BAD_REQUEST.value());
        modelAndView.addObject("errMessage", "유효성 검사 중 오류가 발생했습니다. " + validationError);
        return modelAndView;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ModelAndView handleHandlerMethodValidationException(
            HandlerMethodValidationException e
    ) {
        String validationError = e.getAllErrors().getFirst().getDefaultMessage();
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("statusCode", HttpStatus.BAD_REQUEST.value());
        modelAndView.addObject("errMessage", "유효성 검사 중 오류가 발생했습니다." + validationError);

        return modelAndView;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView handleConstraintViolationException(
            ConstraintViolationException e
    ) {
        String validationError = e.getConstraintViolations().stream()
                .findFirst().map(ConstraintViolation::getMessage)
                .orElse("");

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("statusCode", HttpStatus.BAD_REQUEST.value());
        modelAndView.addObject("errMessage", "유효성 검사 오류: " + validationError);
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(
            Exception e
    ) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        modelAndView.addObject("errMessage", e.getMessage());

        return modelAndView;
    }
}

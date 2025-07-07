package gift.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleProductNotFoundException(
        ProductNotFoundException e) {
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        ExceptionResponseDto exception = new ExceptionResponseDto(errors, 404,
            LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDto> handleIllegalArgumentException(
        IllegalArgumentException e) {

        ExceptionResponseDto response = new ExceptionResponseDto(
            List.of(e.getMessage()),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {

        List<String> errorMessage = e.getBindingResult().getFieldErrors()
            .stream()
            .map(fieldError -> fieldError.getField() + ":" + fieldError.getDefaultMessage())
            .toList();
        ExceptionResponseDto exception = new ExceptionResponseDto(
            errorMessage,
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(KakaoApproveException.class)
    public String handleKakaoApproveException(
        KakaoApproveException e,
        RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage",
            e.getMessage());
        return "redirect:/managerHome";
    }
}



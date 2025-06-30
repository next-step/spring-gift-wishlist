package gift.controller;

import gift.dto.ErrorMessageResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping(value =  "/error", produces = "text/html")
    public String handleViewError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        if (status != null && message != null) {
            model.addAttribute("statusCode",status);
            model.addAttribute("errorMessage", message.toString());
        }
        else {
            model.addAttribute("statusCode", 500);
            model.addAttribute("errorMessage", "알 수 없는 오류가 발생했습니다.");
        }
        return "error";
    }

    @RequestMapping(value = "/error", produces = "application/json")
    public ResponseEntity<ErrorMessageResponse> handleApiError(HttpServletRequest request) {
        Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        HttpStatus status = (HttpStatus) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        var errorMessage = ErrorMessageResponse.generateFrom(
                request, exception != null ? exception : new Exception("알 수 없는 오류가 발생했습니다."), status
        );

        return new ResponseEntity<>(errorMessage, status);
    }
}

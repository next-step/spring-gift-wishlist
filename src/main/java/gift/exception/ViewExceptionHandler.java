package gift.exception;

import gift.controller.MemberViewController;
import gift.controller.ProductViewController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(assignableTypes = {ProductViewController.class, MemberViewController.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ViewExceptionHandler {

    @ExceptionHandler(ProductNotExistException.class)
    public ModelAndView handleProductNotExist() {

        Map<String, String> model = new HashMap<>();
        model.put("errorMessage", "상품이 존재하지 않습니다.");

        return new ModelAndView("error/product-not-found", model);
    }

    @ExceptionHandler(MemberNotExistException.class)
    public ModelAndView handleMemberNotExist() {

        Map<String, String> model = new HashMap<>();
        model.put("errorMessage", "회원이 존재하지 않습니다.");

        return new ModelAndView("error/member-not-exist", model);
    }
}

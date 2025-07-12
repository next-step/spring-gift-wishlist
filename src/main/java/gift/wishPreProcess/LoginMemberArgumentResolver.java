package gift.wishPreProcess;

import gift.auth.JwtTokenHandler;
import gift.entity.User;
import gift.exception.UserNotFoundException;
import gift.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenHandler jwtTokenHandler;
    private final UserService userService;

    public LoginMemberArgumentResolver(JwtTokenHandler jwtTokenHandler, UserService userService) {
        this.jwtTokenHandler = jwtTokenHandler;
        this.userService = userService;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        Optional<User> user = userService.getUserByEmail(
            jwtTokenHandler.getEmailFromHeader(
                request.getHeader("Authorization")));
        return user.orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다"));
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class)
            && parameter.getParameterType().equals(User.class);
    }
}

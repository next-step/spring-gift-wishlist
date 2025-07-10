package gift.annotation;

import gift.jwt.JwtTokenProvider;
import gift.service.UserService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginUserArgumentResolver(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory
                    binderFactory) throws Exception {
        String authHeader = webRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization 헤더가 없거나 잘못됨");
        }
        String token = authHeader.substring(7);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new RuntimeException("유효하지 않은 토큰");
        }
        String userId = jwtTokenProvider.getUserIdFromToken(token);

        return userService.findByUserId(userId);
    }
}
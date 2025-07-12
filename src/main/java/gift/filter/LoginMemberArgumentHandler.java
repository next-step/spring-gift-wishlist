package gift.filter;

import gift.annotation.AuthUser;
import gift.exception.token.NoTokenException;
import gift.service.TokenService;
import gift.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static gift.status.TokenErrorStatus.*;

@Component
public class LoginMemberArgumentHandler implements HandlerMethodArgumentResolver {
    private final UserService userService;
    private final TokenService tokenService;

    public LoginMemberArgumentHandler(
            UserService userService,
            TokenService tokenService
    ) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUser.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest req = webRequest.getNativeRequest(HttpServletRequest.class);
        if(req != null){
            final String authorization = req.getHeader("Authorization");
            if (authorization == null) {
                return null;
            }
            final String token = authorization.substring(7);
            if(token.isEmpty()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(INVALID_TOKEN_TYPE);
            }

            if(tokenService.isTokenExpired(token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(TOKEN_EXPIRED);
            }
            String email = tokenService.extractEmail(token);
            return userService.getUserInfo(email);
        }
        return new NoTokenException(NO_TOKEN.getErrorMessage());
    }
}

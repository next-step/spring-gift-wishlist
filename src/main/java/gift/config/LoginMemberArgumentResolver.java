package gift.config;

import gift.util.JwtTokenProvider;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
         String authorizationHeader = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

         if (authorizationHeader == null || !authorizationHeader.startsWith(AUTHORIZATION_PREFIX)) {
             throw new AuthenticationException("Invalid Authorization header");
         }

         String token = authorizationHeader.substring(AUTHORIZATION_PREFIX.length());
        return jwtTokenProvider.parseJwtToken(token).toMember();
    }
}

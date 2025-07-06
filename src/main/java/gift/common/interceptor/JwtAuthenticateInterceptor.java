package gift.common.interceptor;

import gift.common.model.CustomAuth;
import gift.common.util.TokenProvider;
import gift.entity.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.naming.AuthenticationException;

@Component
public class JwtAuthenticateInterceptor implements HandlerInterceptor {
    private final TokenProvider tokenProvider;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public JwtAuthenticateInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        String token = extractToken(authorizationHeader);

        if (token == null) {
            request.setAttribute("auth", new CustomAuth(null, UserRole.ROLE_GUEST));
            return true; // 토큰이 없으면 인증을 건너뜁니다.
        }

        if (!tokenProvider.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new AuthenticationException("유효하지 않은 토큰입니다. 올바른 토큰을 제공해야 합니다.");
        }
        CustomAuth auth = tokenProvider.getAuthentication(token);
        if (auth == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new AuthenticationException("토큰에서 인증 정보를 가져올 수 없습니다. 올바른 토큰을 제공해야 합니다.");
        }
        request.setAttribute("auth", auth);
        return true;
    }
}

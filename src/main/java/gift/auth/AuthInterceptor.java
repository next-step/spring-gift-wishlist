package gift.auth;

import gift.exception.AuthenticationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {

        String token = extractTokenFromHeader(request);

        if (token == null) {
            token = extractTokenFromCookie(request).orElse(null);
        }

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            if (request.getRequestURI().startsWith("/admin")) {
                response.sendRedirect("/admin/login");
                return false;
            }
            throw new AuthenticationException("유효하지 않은 토큰입니다.");
        }

        request.setAttribute("userEmail", jwtTokenProvider.getPayload(token));
        return true;
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private Optional<String> extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> "auth_token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}

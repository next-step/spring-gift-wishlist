package gift.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private static final String BEARER_PREFIX = "Bearer ";

    public JwtAuthInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler)
            throws IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            handleUnauthorized(response, "인증 헤더가 없거나 형식이 올바르지 않습니다.");
            return false;
        }

        try {
            String token = authHeader.substring(BEARER_PREFIX.length());
            Claims claims = jwtProvider.parseClaims(token);
            request.setAttribute("memberId", claims.getSubject());
            return true;
        } catch (ExpiredJwtException e) {
            handleUnauthorized(response, "만료된 토큰입니다.");
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            handleUnauthorized(response, "유효하지 않은 토큰입니다.");
            return false;
        }
    }

    private void handleUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format("{\"error\": \"%s\"}", message));
    }
}

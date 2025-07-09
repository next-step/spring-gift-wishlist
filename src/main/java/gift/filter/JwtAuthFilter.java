package gift.filter;

import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 모든 /api/** 요청에 대해 Bearer 토큰을 검증하고 JSON 에러를 직접 응답으로 작성
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/api/members/register") || path.startsWith("/api/members/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            writeError(response, "유효한 Bearer 토큰이 필요합니다.");
            return;
        }
        String token = header.substring(7).trim();
        try {
            Jws<Claims> jws = JwtUtil.parseToken(token);
            request.setAttribute("authClaims", jws.getPayload());
        } catch (JwtException ex) {
            writeError(response, "유효하지 않은 토큰입니다.");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void writeError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        String body = String.format(
                "{\"status\":%d,\"error\":\"%s\"}",
                HttpServletResponse.SC_UNAUTHORIZED, message
        );
        response.getWriter().write(body);
    }

    public JwtUtil getJwtUtil() {
        return jwtUtil;
    }
}

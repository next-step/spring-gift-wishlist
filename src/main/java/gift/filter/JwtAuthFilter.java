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

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        return uri.startsWith(ctx + "/api/members");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();

        // /api/ 로 시작하는 나머지 요청은 모두 Bearer 검사
        if (uri.startsWith(ctx + "/api/")) {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                writeError(response, "유효한 Bearer 토큰이 필요합니다.");
                return;
            }
            String token = header.substring(7).trim();
            try {
                Jws<Claims> jws = JwtUtil.parseToken(token);  // JwtUtil.parseToken 내부에서 서명+만료 검증
                // 검증된 Claims를 request 속성에 담아두면 컨트롤러에서 꺼낼 수 있습니다.
                request.setAttribute("authClaims", jws.getBody());
            } catch (JwtException ex) {
                writeError(response, "유효하지 않은 토큰입니다.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void writeError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                String.format("{\"code\":\"UNAUTHORIZED\",\"message\":\"%s\"}", message)
        );
    }
}

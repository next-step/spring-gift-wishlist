package gift.filter;

import gift.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * API 요청(Authorization 헤더)용 JWT 필터
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JwtHeaderFilter extends JwtFilter {

    public JwtHeaderFilter(JwtUtil jwtUtil) {
        super(jwtUtil);
    }

    @Override
    protected boolean shouldFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        // /api/members/** 경로는 인증 제외
        if (uri.startsWith(ctx + "/api/members")) {
            return false;
        }
        return uri.startsWith(ctx + "/api/");
    }

    @Override
    protected String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7).trim();
        }
        return null;
    }

    @Override
    protected void writeError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                String.format("{\"code\":\"UNAUTHORIZED\",\"message\":\"%s\"}", message)
        );
    }
}

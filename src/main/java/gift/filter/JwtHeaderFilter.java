package gift.filter;

import gift.util.BearerAuthUtil;
import gift.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JwtHeaderFilter extends JwtFilter {

    private final BearerAuthUtil bearerAuthUtil;

    public JwtHeaderFilter(JwtUtil jwtUtil, BearerAuthUtil bearerAuthUtil) {
        super(jwtUtil);
        this.bearerAuthUtil = bearerAuthUtil;
    }

    @Override
    protected boolean shouldFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
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

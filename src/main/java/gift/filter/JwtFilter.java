package gift.filter;

import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public abstract class JwtFilter extends OncePerRequestFilter {

    protected final JwtUtil jwtUtil;

    protected JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    protected abstract boolean shouldFilter(HttpServletRequest request);

    protected abstract String resolveToken(HttpServletRequest request);

    protected abstract void writeError(HttpServletResponse response, String message)
            throws IOException;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain chain
    ) throws ServletException, IOException {
        if (!shouldFilter(request)) {
            chain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);
        if (token == null || token.isBlank()) {
            writeError(response, "유효한 토큰이 필요합니다.");
            return;
        }

        try {
            Jws<Claims> jws = jwtUtil.parseToken(token);
            request.setAttribute("authClaims", jws.getPayload());
        } catch (JwtException ex) {
            writeError(response, "유효하지 않은 토큰입니다.");
            return;
        }

        chain.doFilter(request, response);
    }
}

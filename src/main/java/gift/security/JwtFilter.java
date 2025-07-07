package gift.security;

import gift.exception.UnauthorizedException;
import gift.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (jwtUtil.isValidToken(token)) {
                Long memberId = jwtUtil.getMemberIdFromToken(token);
            } else {
                throw new UnauthorizedException("유효하지 않은 토큰입니다.");
            }

        } catch (Exception e) {
            throw new UnauthorizedException("토큰 검증 중 오류가 발생했습니다.");
        }

        filterChain.doFilter(request, response);
    }
}

package gift.config;

import gift.exception.ForbiddenAccessException;
import gift.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = jwtService.extractTokenFromBearer(authHeader);

        // 특정 URI만 필터링
        if (requiresAuth(request.getRequestURI())) {
            if (!StringUtils.hasText(token) || !jwtService.validateToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 에러 발생
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/plain; charset=UTF-8");
                response.getWriter().write("인증이 필요합니다.");
                return;
            }
            
            // admin 경로에 대한 권한 확인
            if (request.getRequestURI().startsWith("/admin")) {
                String role = jwtService.extractRole(token);
                if (!"ADMIN".equals(role)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 에러 발생
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("text/plain; charset=UTF-8");
                    response.getWriter().write("관리자 권한이 필요합니다.");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean requiresAuth(String uri) {
        return uri.startsWith("/api/products") || uri.startsWith("/admin");
    }
}

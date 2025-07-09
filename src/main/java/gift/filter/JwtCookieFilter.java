package gift.filter;

import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JwtCookieFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtCookieFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getRequestURI();

        // 로그인 페이지나 정적 리소스는 필터링 제외
        if (path.startsWith(request.getContextPath() + "/admin/login") ||
                path.startsWith(request.getContextPath() + "/api/") ||
                path.startsWith(request.getContextPath() + "/css/") ||
                path.startsWith(request.getContextPath() + "/js/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // /admin/** 요청에 대해서만 쿠키에서 토큰 꺼내기
        if (path.startsWith(request.getContextPath() + "/admin/")) {
            String token = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                token = Arrays.stream(cookies)
                        .filter(c -> "AUTH_TOKEN".equals(c.getName()))
                        .findFirst()
                        .map(Cookie::getValue)
                        .orElse(null);
            }

            if (token == null || token.isBlank()) {
                writeError(response, "토큰이 없습니다. 로그인 후 다시 시도하세요.");
                return;
            }

            try {
                Jws<Claims> jws = JwtUtil.parseToken(token);
                // 검증된 클레임을 request 속성에 저장
                request.setAttribute("authClaims", jws.getBody());
            } catch (JwtException ex) {
                writeError(response, "유효하지 않은 토큰입니다.");
                return;
            }
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
}

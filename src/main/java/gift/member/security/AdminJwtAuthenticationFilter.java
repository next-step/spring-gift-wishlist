package gift.member.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/admin/*")
public class AdminJwtAuthenticationFilter implements Filter {

    private final JwtTokenProvider jwtTokenProvider;
    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String BEARER_PREFIX = "Bearer ";

    public AdminJwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain)
        throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

        String token =
            (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX))
                ? authorizationHeader.substring(BEARER_PREFIX.length())
                : null;

        if (token == null) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 없음 또는 잘못된 형식");
            return;
        }

        try {
            Claims claims = Jwts.parser()
                .setSigningKey(jwtTokenProvider.key)
                .build()
                .parseClaimsJws(token)
                .getBody();

            String role = claims.get("role", String.class);

            if (!"ROLE_ADMIN".equals(role)) {
                httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "권한이 없습니다.");
                return;
            }

        } catch (Exception e) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 검증 실패");
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

package gift.auth;

import gift.auth.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. 토큰 추출
        String token = extractToken(request);

        // 2. 토큰 유효성 검증
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            // 3. 토큰에서 회원 ID 추출
            Long memberId = jwtTokenProvider.getMemberId(token);

            // 4. Request에 회원 ID 저장
            request.setAttribute("memberId", memberId);
        }

        // 5. 다음 필터로 진행
        filterChain.doFilter(request, response);
    }

    // Authorization 헤더에서 토큰 추출
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    // 인증이 필요없는 경로는 필터 skip
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        // 인증이 필요없는 경로들
        return path.startsWith("/api/members/register") ||
                path.startsWith("/api/members/login") ||
                path.equals("/");
    }
}
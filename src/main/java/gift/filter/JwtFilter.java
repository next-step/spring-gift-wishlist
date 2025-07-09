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
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 공통 JWT 인증 로직을 추출한 추상 필터
 */
public abstract class JwtFilter extends OncePerRequestFilter {

    protected final JwtUtil jwtUtil;

    /**
     * JwtUtil을 주입받아 필터 생성
     */
    protected JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 이 요청에 필터를 적용할지 여부
     */
    protected abstract boolean shouldFilter(HttpServletRequest request);

    /**
     * 요청에서 JWT 토큰을 추출하는 로직
     */
    protected abstract String resolveToken(HttpServletRequest request);

    /**
     * 인증 실패 시 에러 응답 작성
     */
    protected abstract void writeError(HttpServletResponse response, String message)
            throws IOException;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
        // 필터 적용 여부 확인
        if (!shouldFilter(request)) {
            chain.doFilter(request, response);
            return;
        }

        // 토큰 추출
        String token = resolveToken(request);
        if (token == null || token.isBlank()) {
            writeError(response, "유효한 토큰이 필요합니다.");
            return;
        }

        // 토큰 검증 및 Claims 설정
        try {
            Jws<Claims> jws = jwtUtil.parseToken(token);
            request.setAttribute("authClaims", jws.getBody());
        } catch (JwtException ex) {
            writeError(response, "유효하지 않은 토큰입니다.");
            return;
        }

        chain.doFilter(request, response);
    }
}

package gift.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final List<String> SKIP_PATHS = Arrays.asList(
        "/health",
        "/actuator",
        "/css/",
        "/js/",
        "/images/",
        "/favicon.ico"
    );
    private final JwtTokenPort jwtTokenPort;

    public JwtAuthenticationFilter(JwtTokenPort jwtTokenPort) {
        this.jwtTokenPort = jwtTokenPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        log.info("JWT 필터 처리: {}", requestURI);

        if (shouldSkipFilter(requestURI)) {
            log.info("JWT 필터 건너뜀: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = jwtTokenPort.resolveToken(request);
            
            if (token != null) {
                JwtTokenProvider.TokenValidationResult validationResult = jwtTokenPort.validateToken(token);
                
                if (validationResult.isValid()) {
                    Long memberId = jwtTokenPort.getMemberIdFromToken(token);
                    String email = jwtTokenPort.getEmailFromToken(token);
                    String tokenType = jwtTokenPort.getTokenTypeFromToken(token);
                    String role = jwtTokenPort.getRoleFromToken(token);

                    request.setAttribute("memberId", memberId);
                    request.setAttribute("email", email);
                    request.setAttribute("tokenType", tokenType);
                    request.setAttribute("role", role);
                    request.setAttribute("authenticated", true);
                    
                    log.info("JWT 인증 성공: {} (ID: {}), role: {}", email, memberId, role);
                } else {
                    log.info("유효하지 않은 JWT 토큰: {}", validationResult.getErrorMessage());
                    request.setAttribute("authenticated", false);
                    request.setAttribute("authError", validationResult.getErrorMessage());
                }
            } else {
                log.info("Authorization 헤더에 JWT 토큰이 없음");
                request.setAttribute("authenticated", false);
            }
            
        } catch (Exception e) {
            log.info("JWT 인증 중 오류 발생", e);
            request.setAttribute("authenticated", false);
            request.setAttribute("authError", "인증 오류: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldSkipFilter(String requestURI) {
        return SKIP_PATHS.stream().anyMatch(requestURI::startsWith);
    }
} 
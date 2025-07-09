package gift.global.interceptor;

import gift.global.exception.CustomException;
import gift.global.exception.ErrorCode;
import gift.member.auth.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    public LoginCheckInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // preflight 요청은 통과
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");

        // Authorization 헤더가 없거나 Bearer 형식이 아니면
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.WRONG_HEADER_TOKEN); // 401
        }

        String token = authHeader.substring(7); // "Bearer " 제거
        Claims claims = jwtProvider.parseToken(token); // 유효성 검증 + claims 추출

        // 사용자 ID를 request에 담아서 이후 컨트롤러에서 꺼내 쓸 수 있게
        request.setAttribute("memberId", claims.getSubject());

        return true;
    }
}

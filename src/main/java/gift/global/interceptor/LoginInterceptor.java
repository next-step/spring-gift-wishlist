package gift.global.interceptor;

import gift.global.exception.ErrorCode;
import gift.global.exception.InvalidTokenException;
import gift.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final String BEARER_PREFIX="Bearer ";

    public LoginInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(header==null || !header.startsWith(BEARER_PREFIX)){
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN_REQUEST);
        }

        String token = header.substring(BEARER_PREFIX.length());

        // 예외처리 필요
        // jwtUtil.validateToken(token);
        // request에 memberId를 담아주기
        request.setAttribute("memberId", Long.parseLong(jwtUtil.getSubject(token)));

        return true;
    }
}

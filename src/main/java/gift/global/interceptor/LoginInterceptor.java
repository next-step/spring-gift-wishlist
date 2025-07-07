package gift.global.interceptor;

import gift.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

        String header = request.getHeader("Authorization");

        if(header==null || !header.startsWith(BEARER_PREFIX)){
            throw new RuntimeException("인증 헤더의 형식이 잘못되었습니다.");
        }

        String token = header.substring(BEARER_PREFIX.length());

        // 예외처리 필요
        // jwtUtil.validateToken(token);
        // request에 memberId를 담아주기
        request.setAttribute("memberId", Long.parseLong(jwtUtil.getSubject(token)));

        return true;
    }
}

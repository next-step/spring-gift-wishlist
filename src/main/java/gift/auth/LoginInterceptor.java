package gift.auth;

import gift.member.domain.Member;
import gift.member.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    public LoginInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = AuthUtil.extractToken(request);

        if (token == null) {
            AuthUtil.handleAuthError(request, response, "인증 정보가 없습니다. 다시 로그인해주세요");
            return false;
        }

        if (!jwtUtil.validateToken(token)) {
            AuthUtil.handleAuthError(request, response, "인증 정보가 유효하지 않거나 만료되었습니다. 다시 로그인해주세요.");
            return false;
        }
        request.setAttribute("isLoggedIn", Boolean.TRUE);
        return true;
    }
}

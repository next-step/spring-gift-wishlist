package gift.auth;

import gift.member.domain.Member;
import gift.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.security.sasl.AuthenticationException;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public LoginInterceptor(JwtUtil jwtUtil, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authentication");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthenticationException("인증 헤더가 없거나 형식이 올바르지 않습니다.");
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            throw new AuthenticationException("유효하지 않은 토큰입니다.");
        }

        String email = jwtUtil.getEmail(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("사용자를 찾을 수 없습니다."));

        request.setAttribute("member", member);
        return true;
    }
}

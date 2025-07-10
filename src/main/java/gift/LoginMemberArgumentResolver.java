package gift;

import gift.entity.Member;
import gift.service.MemberService;
import gift.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public LoginMemberArgumentResolver(MemberService memberService,
                                       JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        // 1. HTTP 요청에서 JWT 토큰 추출
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("JWT 토큰이 없습니다.");
        }

        String token = bearerToken.substring(7);

        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 JWT 토큰입니다.");
        }

        // 2. JWT 토큰에서 사용자 이메일 추출
        String email = jwtUtil.extractEmail(token);

        // 3. 이메일로 사용자 정보 조회
        Member member = memberService.findByEmail(email);

        if (member == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        return member;
    }
}

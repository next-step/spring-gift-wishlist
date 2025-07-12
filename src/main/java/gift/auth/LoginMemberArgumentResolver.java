package gift.auth;

import gift.entity.Member;
import gift.service.MemberService;
import gift.exception.unauthorized.WrongHeaderException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BEARER_PREFIX = "Bearer ";

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public LoginMemberArgumentResolver(MemberService memberService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            throw new WrongHeaderException();
        }

        String token = authorization.substring(BEARER_PREFIX.length());

        // JwtProvider를 통해 Claims 파싱
        Claims claims = jwtProvider.parseToken(token);

        // Claims에서 memberId 가져오기
        Long memberId = Long.valueOf(claims.getSubject());

        Member member = memberService.findById(memberId);
        if (member == null) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }
        return member;
    }
}

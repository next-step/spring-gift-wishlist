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

        Claims claims = jwtProvider.parseToken(token);

        Long memberId = Long.valueOf(claims.getSubject());

        Member member = memberService.findByToken(token);
        if (member == null) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        return member;
    }
}

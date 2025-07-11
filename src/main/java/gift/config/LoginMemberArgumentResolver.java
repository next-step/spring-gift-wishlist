package gift.config;

import gift.exception.UnAuthenticationException;
import gift.service.MemberService;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    public LoginMemberArgumentResolver(JwtProvider jwtProvider, MemberService memberService) {
        this.jwtProvider = jwtProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UnAuthenticationException("토큰 형식이 올바르지 않습니다.");
        }

        String token = authorization.split("Bearer ")[1];
        Long memberId = jwtProvider.getMemberIdFromToken(token);

        return memberService.getMemberById(memberId);
    }
}

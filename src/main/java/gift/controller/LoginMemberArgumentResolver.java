package gift.controller;

import gift.entity.Member;
import gift.jwt.Autheniticated;
import gift.jwt.JwtTokenProvider;
import gift.service.MemberService;
import gift.ExceptionHandler.UnAuthorizationException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberArgumentResolver(MemberService memberService, JwtTokenProvider jwtTokenProvider){
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Autheniticated.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String token = checkAuthorization(webRequest);

        if(!jwtTokenProvider.validateToken(token)) {
            throw new UnAuthorizationException("유효하지 않은 토큰입니다.");
        }

        String email = jwtTokenProvider.getEmailFromToken(token);

        // 사용자 조회
        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new UnAuthorizationException("해당 사용자를 찾을 수 없습니다."));

        // memberId만 반환
        return member.getId();
    }

    private String checkAuthorization(NativeWebRequest webRequest) {
        var authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || authorization.isBlank()) {
            throw new RuntimeException("Authorization 헤더가 없습니다.");
        }

        if (!authorization.startsWith("Bearer ")) {
            throw new UnAuthorizationException("Authorization 헤더 형식이 올바르지 않습니다.");
        }

        return authorization.substring(7);
    }
}

package gift.wish.argumentresolver;

import gift.member.repository.MemberRepository;
import gift.member.security.JwtTokenProvider;
import gift.wish.annotation.LoginMember;
import gift.wish.exception.UnauthorizedException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginMembberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginMembberArgumentResolver(MemberRepository memberRepository,
        JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
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

        String authorizationHeader = webRequest.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Authorization 헤더가 없거나 형식이 잘못되었습니다.");
        }

        String token = authorizationHeader.substring(7);

        Long memberId = jwtTokenProvider.getMemberIdFromToken(token);

        try {
            return memberRepository.findMemberById(memberId);
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException("회원이 존재하지 않습니다.");
        }
    }
}

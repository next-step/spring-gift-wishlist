package gift.resolver;

import gift.annotation.LoginMember;
import gift.config.JwtProvider;
import gift.dto.AuthenticatedMemberDto;
import gift.dto.MemberResponseDto;
import gift.entity.Member;
import gift.exception.UnauthorizedException;
import gift.service.MemberService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

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
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authHeader = webRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Authorization 헤더가 유효하지 않습니다.");
        }

        String token = authHeader.substring(7);  // "Bearer " 이후
        Long memberId;
        try {
            memberId = jwtProvider.extractMemberId(token);
        } catch (Exception e) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }

        MemberResponseDto memberResponseDto = memberService.findMemberById(memberId);

        return new AuthenticatedMemberDto(
                memberResponseDto.id(),
                memberResponseDto.email(),
                memberResponseDto.role()
        );
    }
}

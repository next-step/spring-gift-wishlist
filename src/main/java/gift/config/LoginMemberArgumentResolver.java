package gift.config;

import gift.dto.AuthenticatedMemberDto;
import gift.entity.Member;
import gift.exception.UnAuthenticationException;
import gift.service.MemberService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    public LoginMemberArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Long memberId = (Long) webRequest.getAttribute("memberId", RequestAttributes.SCOPE_REQUEST);

        Member authenticatedMember = memberService.getMemberById(memberId)
                                                  .orElseThrow(() -> new UnAuthenticationException(
                                                          "인증되지 않은 사용자입니다"));

        return new AuthenticatedMemberDto(authenticatedMember.getId());
    }
}

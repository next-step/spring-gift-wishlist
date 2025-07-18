package gift.global.resolver;

import gift.global.exception.InvalidTokenException;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    public LoginMemberArgumentResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class)
                && Member.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Object memberIdAttr = request.getAttribute("memberId");

        if (!(memberIdAttr instanceof Long memberId)) {
            throw new InvalidTokenException("요청에 인증된 사용자 ID가 없습니다.");
        }

        return memberRepository.findById(memberId)
                .orElseThrow(() -> new InvalidTokenException("해당 ID에 대한 사용자를 찾을 수 없습니다. (ID: " + memberId + ")"));
    }
}

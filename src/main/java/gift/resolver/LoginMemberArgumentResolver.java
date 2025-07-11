package gift.resolver;

import gift.annotation.LoginMember;
import gift.entity.member.Member;
import gift.entity.member.value.Role;
import gift.service.member.MemberService;
import gift.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    public LoginMemberArgumentResolver(JwtUtil jwtUtil, MemberService memberService) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class)
                && Member.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            @NotNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }
        String token = header.substring(7);
        if (!jwtUtil.validate(token)) {
            throw new IllegalArgumentException("Invalid JWT token");
        }
        Long memberId = jwtUtil.getMemberId(token);
        return memberService.getMemberById(memberId, Role.ADMIN)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }
}

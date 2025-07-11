package gift.auth;

import gift.domain.Member;
import gift.service.MemberService;
import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.method.support.*;
import org.springframework.web.context.request.NativeWebRequest;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    public LoginMemberArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class) &&
                parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header missing");
        }
        String token = header.substring(7);
        if (!JwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }
        Claims claims = JwtUtil.getClaims(token);
        Long memberId = Long.parseLong(claims.getSubject());
        return memberService.findById(memberId).orElseThrow(() -> new RuntimeException("No member"));
    }
}


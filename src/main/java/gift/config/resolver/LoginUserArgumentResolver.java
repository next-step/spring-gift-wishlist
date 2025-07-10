package gift.config.resolver;

import gift.auth.JwtProvider;
import gift.config.annotation.CurrentUser;
import gift.entity.Member;
import gift.exception.common.HttpException;
import gift.exception.unauthorized.WrongHeaderException;
import gift.repository.member.MemberRepository;
import io.jsonwebtoken.Claims;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    
    public LoginUserArgumentResolver(JwtProvider jwtProvider, MemberRepository memberRepository) {
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }
    
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }
    
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws HttpException {
        String authorizationHeader = webRequest.getHeader("Authorization");
        
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new WrongHeaderException();
        }
        
        String token = authorizationHeader.substring(7); // "Bearer " 제거
        Claims claims = jwtProvider.parseToken(token);
        
        String email = claims.get("email", String.class);
        Member member = memberRepository.findMemberByEmail(email);
        
        return member.getId();
    }
}

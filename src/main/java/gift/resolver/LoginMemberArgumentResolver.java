package gift.resolver;

import gift.Entity.Member;
import gift.Jwt.JwtUtil;
import gift.annotation.LoginMember;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;

    public LoginMemberArgumentResolver(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //@LoginMember 어노테이션이 붙어있는가?
        boolean isLoginMemberAnnotation = parameter.hasParameterAnnotation(LoginMember.class);
        // 타입이 Member이거나 그 하위 클래스인가?
        boolean isMemberClass = parameter.getParameterType().isAssignableFrom(Member.class);
        // 모두 만족하면 resolveArgument()를 호출하게됨
        return isLoginMemberAnnotation && isMemberClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        // 현재 HTTP 요청을 가져옴
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        // 요청 헤더 중에서"Authorization" 헤더를 꺼내서 가져옴
        String authHeader = httpServletRequest.getHeader("Authorization");

        // Authorization 헤더가 존재하고 "Bearer"로 시작하는 지 확임
        // Bearer는 토큰을 인증 수단으로 보낸다는 뜻이다. 따라서 실제 인증 정보는 뒤의 토큰이다.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 있다면 "Bearer" 문자열을 제거하여 토큰만 남김
            String token = authHeader.substring(7);
            try {
                // JWT는 3부분으로 구성됨
                // Header : 토큰의 타입, 서명 알고리즘 등
                // payload : 사용자 정보
                // signature : 서버가 서명한 결과값
                // 이중에 payload가 claim에 해당함
                Claims claims = jwtUtil.parseToken(token);

                Member member = new Member();
                member.setId(claims.getSubject());
                member.setName((String) claims.get("name"));
                member.setEmail((String) claims.get("email"));
                member.setAddress((String) claims.get("address"));
                member.setRole((String) claims.get("role"));

                return member;
            } catch (Exception e) {
                // 토큰이 유효하지 않다면 사용자의 정보가 없는 것이니 null을 반환
                return null;
            }
        }

        return null;
    }
}

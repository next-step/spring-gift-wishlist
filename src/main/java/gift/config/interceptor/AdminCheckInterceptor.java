package gift.config.interceptor;

import gift.auth.JwtProvider;
import gift.config.annotation.AdminCheck;
import gift.entity.Member;
import gift.entity.Role;
import gift.exception.common.HttpException;
import gift.exception.forbidden.WrongPermissionException;
import gift.exception.unauthorized.WrongHeaderException;
import gift.repository.member.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminCheckInterceptor implements HandlerInterceptor {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    
    public AdminCheckInterceptor(JwtProvider jwtProvider, MemberRepository memberRepository) {
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws HttpException {
        
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        AdminCheck adminCheck = handlerMethod.getMethodAnnotation(AdminCheck.class);
        
        if(adminCheck == null) {
            return true;
        }
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new WrongHeaderException();
        }
        
        String token = authHeader.substring(7); // "Bearer " 제거
        Claims claims = jwtProvider.parseToken(token);
        
        String email = claims.get("email", String.class);
        Member member = memberRepository.findMemberByEmail(email);
        
        Role role = Role.valueOf(claims.get("role", String.class));
        
        if(!role.equals(Role.ADMIN) || !role.equals(member.getRole())) {
            throw new WrongPermissionException();
        }
        
        return true;
    }
}

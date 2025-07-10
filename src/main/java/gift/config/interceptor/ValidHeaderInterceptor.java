package gift.config.interceptor;

import gift.auth.JwtProvider;
import gift.config.annotation.ValidHeader;
import gift.entity.Role;
import gift.exception.common.HttpException;
import gift.exception.forbidden.WrongPermissionException;
import gift.exception.unauthorized.WrongHeaderException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ValidHeaderInterceptor implements HandlerInterceptor {
    private final JwtProvider jwtProvider;
    
    public ValidHeaderInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws HttpException {
        
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        ValidHeader validHeader = handlerMethod.getMethodAnnotation(ValidHeader.class);
        
        if(validHeader == null) {
            return true;
        }
        
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new WrongHeaderException();
        }
        
        
        String token = authHeader.substring(7);
        Claims claims = jwtProvider.parseToken(token);
        request.setAttribute("email", claims.get("email", String.class));
        
        
        Role requiredRole = validHeader.role();
        if (requiredRole != Role.NONE) {
            Role tokenRole = Role.valueOf(claims.get("role", String.class));
            if (!tokenRole.equals(requiredRole)) {
                throw new WrongPermissionException();
            }
        }
        
        return true;
    }
}

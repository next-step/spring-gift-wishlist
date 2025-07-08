package gift.config.interceptor;

import gift.config.annotation.AdminCheck;
import gift.exception.common.HttpException;
import gift.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminCheckInterceptor implements HandlerInterceptor {
    private final AuthService authService;
    
    public AdminCheckInterceptor(AuthService authService) {
        this.authService = authService;
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
        
        authService.checkPermissonForAdmin(authHeader);
        
        return true;
    }
}

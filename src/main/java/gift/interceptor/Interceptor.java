package gift.interceptor;

import gift.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class Interceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    public Interceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    //controller 실행 직전
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        System.out.println(token);
        if (token != null) {
            try {
                jwtProvider.validateToken(token);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "유효하지 않은 토큰입니다.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization 헤더가 없습니다.");
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    //controller 실행 직후 && view 처리 직전
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    //view 실행 직후
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}

package yjshop.interceptor;

import groovy.util.logging.Slf4j;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yjshop.service.AuthServiceJWTandCookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoginChecker implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoginChecker.class);
    private final AuthServiceJWTandCookie authServiceJWTandCookie;

    public LoginChecker(AuthServiceJWTandCookie authServiceJWTandCookie) {
        this.authServiceJWTandCookie = authServiceJWTandCookie;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //컨트롤러가 호출되기 전에 실행됨
        log.info("[interceptor] preHandle");

        String token = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie c : cookies){
            if(c.getName().equals("yjtoken")){
                token = c.getValue();
            }
        }

        //토큰이 없다면 -> 로그인 페이지로 이동시키기
        if(token == null){
            response.sendRedirect("/view/loginform");
            return false; //컨트롤러가 동작하지 않는다.
        }

        //토큰의 유효성을 확인하기
        authServiceJWTandCookie.checkValidation(token);
        return true; //컨트롤러가 동작
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {;
        log.info("[interceptor] postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        log.info("[interceptor] afterCompletion");
    }

}

package gift.yjshop.interceptor;

import gift.yjshop.service.AuthServiceJWTandCookie;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
    private final AuthServiceJWTandCookie authServiceJWTandCookie;

    public LoginInterceptor(AuthServiceJWTandCookie authServiceJWTandCookie) {
        this.authServiceJWTandCookie = authServiceJWTandCookie;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {;
        log.info("[interceptor] postHandle");

        if(response.getStatus() == HttpServletResponse.SC_CREATED || response.getStatus() == HttpServletResponse.SC_CREATED){
            log.info("로그인 인증 중....");

            //JWT토큰 발급을 위해 필요한 정보들(memberId)
            Long memberId = Long.parseLong(request.getAttribute("memberId").toString());
            String token = authServiceJWTandCookie.createJwt(request.getParameter("email"), memberId);

            //쿠키 발행
            Cookie tcookie = new Cookie("yjtoken", token);
            tcookie.setPath("/");
            response.addCookie(tcookie);
        }
    }

}

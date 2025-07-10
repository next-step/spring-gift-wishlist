package yjshop;

import jakarta.servlet.http.Cookie;
import yjshop.service.AuthServiceJWTandCookie;
import yjshop.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoggedInUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final AuthServiceJWTandCookie authServiceJWTandCookie;

    public LoggedInUserArgumentResolver(
            MemberService memberService, AuthServiceJWTandCookie authServiceJWTandCookie) {
        this.memberService = memberService;
        this.authServiceJWTandCookie = authServiceJWTandCookie;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoggedInUser.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception
    {
        //쿠키 가져오기
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();

        String token = null;
        Cookie[] cookies = servletRequest.getCookies();
        for(Cookie c : cookies){
            if(c.getName().equals("yjtoken")){
                token = c.getValue();
            }
        }

        //쿠키에 저장된 토큰으로 부터 사용자 정보 가져오기
        Long id = authServiceJWTandCookie.getMemberId(token);
        return memberService.findMember(id).get();
    }
}

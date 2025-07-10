package yjshop;

import yjshop.interceptor.LoginInterceptor;
import yjshop.interceptor.LoginChecker;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final LoginChecker loginChecker;
    private final LoggedInUserArgumentResolver loggedInUserArgumentResolver;


    public WebMvcConfig(
            LoginInterceptor loginInterceptor,
            LoginChecker loginChecker,
            LoggedInUserArgumentResolver loggedInUserArgumentResolver
    ) {
        this.loginInterceptor = loginInterceptor;
        this.loginChecker = loginChecker;
        this.loggedInUserArgumentResolver = loggedInUserArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loggedInUserArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor)
                        .addPathPatterns("/view/register", "/view/login");

        registry.addInterceptor(loginChecker)
                .addPathPatterns("/view/my/**");
        //addPathPatterns : /view/my/blah~blah~blah~
        //.excludePathPatterns()
    }

}

package gift.yjshop;

import gift.yjshop.interceptor.AdminChecker;
import gift.yjshop.interceptor.LoginInterceptor;
import gift.yjshop.interceptor.LoginChecker;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class YjConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final LoginChecker loginChecker;
    private final AdminChecker adminChecker;
    private final YjArgumentResolver yjArgumentResolver;


    public YjConfig(
            LoginInterceptor loginInterceptor,
            LoginChecker loginChecker, AdminChecker adminChecker,
            YjArgumentResolver yjArgumentResolver
    ) {
        this.loginInterceptor = loginInterceptor;
        this.loginChecker = loginChecker;
        this.adminChecker = adminChecker;
        this.yjArgumentResolver = yjArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(yjArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor)
                        .addPathPatterns("/view/register", "/view/login");

        registry.addInterceptor(loginChecker)
                .addPathPatterns("/view/my/**");

        registry.addInterceptor(adminChecker)
                .addPathPatterns("/view/admin/**");
        //addPathPatterns : /view/my/blah~blah~blah~
        //.excludePathPatterns()
    }

}

package gift;

import gift.interceptor.LoginCheckInterceptor;
import gift.filter.LoginFilter;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //private final LoginFilter loginFilter;
    private final LoginCheckInterceptor loginCheckInterceptor;
    private final LoggedInMemberArgumentResolver loggedInMemberArgumentResolver;

    public WebConfig(
            LoginCheckInterceptor loginCheckInterceptor,
            LoggedInMemberArgumentResolver loggedInMemberArgumentResolver
    ) {
        this.loginCheckInterceptor = loginCheckInterceptor;
        this.loggedInMemberArgumentResolver = loggedInMemberArgumentResolver;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/api/wishlist/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loggedInMemberArgumentResolver);
    }


}

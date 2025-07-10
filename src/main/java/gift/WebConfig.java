package gift;

import gift.interceptor.LoginCheckInterceptor;
import gift.interceptor.LoginFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginFilter loginFilter;
    private final LoginCheckInterceptor loginCheckInterceptor;

    public WebConfig(
            LoginFilter loginFilter,
            LoginCheckInterceptor loginCheckInterceptor
    ) {
        this.loginFilter = loginFilter;
        this.loginCheckInterceptor = loginCheckInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/api/wishlist/**");

    }


}

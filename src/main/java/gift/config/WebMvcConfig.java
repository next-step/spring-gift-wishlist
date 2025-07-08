package gift.config;

import gift.config.interceptor.AdminCheckInterceptor;
import gift.config.resolver.LoginUserArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AdminCheckInterceptor adminCheckInterceptor;
    private final LoginUserArgumentResolver loginUserArgumentResolver;
    
    public WebMvcConfig(AdminCheckInterceptor adminCheckInterceptor,
        LoginUserArgumentResolver loginUserArgumentResolver) {
        this.adminCheckInterceptor = adminCheckInterceptor;
        this.loginUserArgumentResolver = loginUserArgumentResolver;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminCheckInterceptor);
    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }
}

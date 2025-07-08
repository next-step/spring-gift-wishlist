package gift.config;

import gift.config.interceptor.AdminCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AdminCheckInterceptor adminCheckInterceptor;
    
    public WebMvcConfig(AdminCheckInterceptor adminCheckInterceptor) {
        this.adminCheckInterceptor = adminCheckInterceptor;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminCheckInterceptor);
    }
}

package gift.config;

import gift.interceptor.AdminCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AdminCheckInterceptor adminCheckInterceptor;

    public WebConfig(AdminCheckInterceptor adminCheckInterceptor) {
        this.adminCheckInterceptor = adminCheckInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminCheckInterceptor)
                .addPathPatterns("/admin/**");
    }
}

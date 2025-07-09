package gift.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import gift.interceptor.AdminAuthInterceptor;
import gift.interceptor.UserAuthInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserAuthInterceptor userAuthInterceptor;
    private final AdminAuthInterceptor adminAuthInterceptor;

    public WebMvcConfig(
        UserAuthInterceptor userAuthInterceptor,
        AdminAuthInterceptor adminAuthInterceptor
    ) {
        this.userAuthInterceptor = userAuthInterceptor;
        this.adminAuthInterceptor = adminAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userAuthInterceptor)
            .addPathPatterns("/api/products/**");

        registry.addInterceptor(adminAuthInterceptor)
            .addPathPatterns("/admin/**"); // `ROLE_ADMIN` 권한의 JWT 토큰을 가진 사용자만 인가
    }
}

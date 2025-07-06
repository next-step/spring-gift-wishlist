package gift.config;

import gift.common.interceptor.JwtAuthenticateInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final JwtAuthenticateInterceptor jwtAuthenticateInterceptor;

    public WebConfig(JwtAuthenticateInterceptor jwtAuthenticateInterceptor) {
        this.jwtAuthenticateInterceptor = jwtAuthenticateInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticateInterceptor)
                .addPathPatterns("/**") // 모든 경로에 대해 인터셉터 적용
                .excludePathPatterns("/api/auth/**", "/error"); // 인증 관련 경로는 제외
    }
}

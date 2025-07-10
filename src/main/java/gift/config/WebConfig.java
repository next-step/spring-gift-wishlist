package gift.config;

import gift.security.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**") // 전체 API 보호
                .excludePathPatterns(
                        "/api/members/register", // 회원가입 허용
                        "/api/members/login",    // 로그인 허용
                        "/error",                // 오류 응답 허용
                        "/swagger-ui/**",        // Swagger 접근 허용
                        "/v3/api-docs/**"
                );
    }
}

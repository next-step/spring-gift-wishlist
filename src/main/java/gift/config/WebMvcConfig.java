package gift.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import gift.interceptor.AdminAuthInterceptor;
import gift.interceptor.MemberAuthInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberAuthInterceptor memberAuthInterceptor;
    private final AdminAuthInterceptor adminAuthInterceptor;

    public WebMvcConfig(
        MemberAuthInterceptor memberAuthInterceptor,
        AdminAuthInterceptor adminAuthInterceptor
    ) {
        this.memberAuthInterceptor = memberAuthInterceptor;
        this.adminAuthInterceptor = adminAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(memberAuthInterceptor)
            .addPathPatterns("/api/products/**");

        registry.addInterceptor(adminAuthInterceptor)
            .addPathPatterns("/admin/**"); // `ROLE_ADMIN` 권한의 JWT 토큰을 가진 사용자만 인가
    }
}

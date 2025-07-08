package gift.config;

import gift.interceptor.HostInterceptor;
import gift.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    private final HostInterceptor hostInterceptor;

    public WebConfig(JwtInterceptor jwtInterceptor,
                     HostInterceptor hostInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
        this.hostInterceptor = hostInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 로그인 검증이 필요한 api 추가
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("")
                .excludePathPatterns("/**");

        registry.addInterceptor(hostInterceptor)
                .addPathPatterns("/**");
    }
}


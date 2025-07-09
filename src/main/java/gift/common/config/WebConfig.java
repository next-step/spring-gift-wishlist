package gift.common.config;

import gift.common.security.AuthorizationAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthorizationAspect authorizationAspect;

    public WebConfig(AuthorizationAspect authorizationAspect) {
        this.authorizationAspect = authorizationAspect;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationAspect);
    }
} 
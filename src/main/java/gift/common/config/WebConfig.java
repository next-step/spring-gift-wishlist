package gift.common.config;

import gift.common.security.AuthorizationAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Configuration
    @Profile("!test")
    public static class SecurityWebConfig implements WebMvcConfigurer {
        
        private final AuthorizationAspect authorizationAspect;

        public SecurityWebConfig(AuthorizationAspect authorizationAspect) {
            this.authorizationAspect = authorizationAspect;
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(authorizationAspect);
        }
    }

    @Configuration
    @Profile("test")
    public static class TestWebConfig implements WebMvcConfigurer {
    }
} 
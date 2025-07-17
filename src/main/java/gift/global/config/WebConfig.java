package gift.global.config;

import gift.global.interceptor.AuthInterceptor;
import gift.global.resolver.LoginMemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;

    public WebConfig(AuthInterceptor authInterceptor, LoginMemberArgumentResolver loginMemberArgumentResolver) {
        this.authInterceptor = authInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**", "/admin/wishes")
                .excludePathPatterns("/api/members/register", "/api/members/login","/admin/products");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }
}
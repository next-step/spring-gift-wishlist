package gift.config;

import gift.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import gift.login.LoginMemberArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import java.util.List;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;

    public WebMvcConfig(AuthenticationInterceptor authenticationInterceptor,
        LoginMemberArgumentResolver loginMemberArgumentResolver) {
        this.authenticationInterceptor = authenticationInterceptor;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
            .addPathPatterns("/api/**", "/admin/**")
            .excludePathPatterns("/api/members/register", "/api/members/login", "/api/products");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }
}
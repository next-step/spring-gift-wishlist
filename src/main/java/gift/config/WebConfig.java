package gift.config;

import gift.auth.JwtAuthenticationFilter;
import gift.resolver.LoginMemberArgumentResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthenticationFilter filter;
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;

    public WebConfig(JwtAuthenticationFilter filter, LoginMemberArgumentResolver loginMemberArgumentResolver) {
        this.filter = filter;
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/api/*", "/wishes/*");
        return registrationBean;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }
}

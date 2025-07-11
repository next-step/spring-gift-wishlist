package gift.config;

import gift.filter.JwtAuthenticationFilter;

import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> jwtFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtAuthenticationFilter());
        registration.addUrlPatterns("/*"); // 전체 경로 감시
        registration.setOrder(1);
        return registration;
    }
}

package gift.common.config;

import gift.common.filter.ExceptionHandlerFilter;
import gift.common.filter.JwtAuthenticationFilter;
import gift.service.JwtTokenProvider;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private static final String[] URL_PATTERNS = new String[]{
            "/admin/*",
            "/api/users/password",
            "/api/users/admin"
    };

    @Bean
    public FilterRegistrationBean<Filter> registerExceptionHandlerFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ExceptionHandlerFilter());
        bean.setOrder(1);
        bean.addUrlPatterns(URL_PATTERNS);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<Filter> registerJwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new JwtAuthenticationFilter(jwtTokenProvider));
        bean.setOrder(2);
        bean.addUrlPatterns(URL_PATTERNS);
        return bean;
    }
}

package gift.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.jwt.JWTUtil;
import gift.jwt.filter.CustomLoginFilter;
import gift.member.service.MemberService;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    private final MemberService memberService;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;


    public SecurityConfig(MemberService memberService, JWTUtil jwtUtil, ObjectMapper objectMapper) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Bean
    public FilterRegistrationBean customLoginFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new CustomLoginFilter(memberService, jwtUtil, objectMapper));

        filterFilterRegistrationBean.addUrlPatterns("/login");
        filterFilterRegistrationBean.setOrder(1);
        return filterFilterRegistrationBean;
    }
}

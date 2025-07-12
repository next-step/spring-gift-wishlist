package gift.global.config;

import gift.global.interceptor.LoginCheckInterceptor;
import gift.global.resolver.LoginMemberArgumentResolver;
import gift.member.auth.JwtProvider;
import gift.member.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginCheckInterceptor loginCheckInterceptor;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public WebConfig(LoginCheckInterceptor loginCheckInterceptor,
                     JwtProvider jwtProvider,
                     MemberRepository memberRepository) {
        this.loginCheckInterceptor = loginCheckInterceptor;
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/api/members/register",
                        "/api/members/login"
                );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(jwtProvider, memberRepository));
    }
}

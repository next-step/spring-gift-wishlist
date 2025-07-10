package gift.wish.config;

import gift.member.repository.MemberRepository;
import gift.member.security.JwtTokenProvider;
import gift.wish.argumentresolver.LoginMembberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public WebConfig(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(
        List<HandlerMethodArgumentResolver> argumentResolvers
    ) {
        argumentResolvers.add(new LoginMembberArgumentResolver(memberRepository, jwtTokenProvider));
    }

}

package gift.config;

import gift.annotation.LoginUserArgumentResolver;
import gift.jwt.JwtTokenProvider;
import gift.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    public WebConfig(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
    this.jwtTokenProvider = jwtTokenProvider;}
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
        resolvers.add(new LoginUserArgumentResolver(userService,jwtTokenProvider));
    }
}
package gift.configuration;

import gift.resolver.TokenResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenResolver tokenResolver;

    public WebMvcConfig(TokenResolver tokenResolver) {
        this.tokenResolver = tokenResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(tokenResolver);
    }
}

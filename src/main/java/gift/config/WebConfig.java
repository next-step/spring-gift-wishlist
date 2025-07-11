package gift.config;

import gift.handler.CustomArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CustomArgumentResolver customArgumentResolver;

    public WebConfig(CustomArgumentResolver customArgumentResolver) {
        this.customArgumentResolver = customArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(customArgumentResolver);
    }
}

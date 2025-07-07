package gift.common.config;

import gift.domain.Role;
import gift.domain.User;
import gift.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminUserInitializer {

    private final UserRepository userRepository;

    public AdminUserInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            userRepository.save(new User("admin@admin.com", "1234", Role.ADMIN));
        };
    }
}

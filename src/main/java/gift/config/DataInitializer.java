package gift.config;

import gift.api.domain.Member;
import gift.api.domain.MemberRole;
import gift.api.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String adminEmail = "admin@admin";
        String adminPassword = "admin123";

        if (memberRepository.findByEmail(adminEmail).isEmpty()) {
            Member admin = new Member(
                    null,
                    adminEmail,
                    passwordEncoder.encode(adminPassword),
                    MemberRole.ADMIN
            );
            memberRepository.registerMember(admin);
        }
    }
}
package gift.component;

import gift.domain.Member;
import gift.enums.Role;
import gift.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MockDataInitializer implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final BCryptEncryptor bCryptEncryptor;

    public MockDataInitializer(MemberRepository memberRepository, BCryptEncryptor bCryptEncryptor) {
        this.memberRepository = memberRepository;
        this.bCryptEncryptor = bCryptEncryptor;
    }

    @Override
    public void run(String... args) throws Exception {
        memberRepository.registerMember(new Member("test1@email.com", bCryptEncryptor.encode("1234"), Role.ROLE_USER));
        memberRepository.registerMember(new Member("test2@email.com", bCryptEncryptor.encode("1234"), Role.ROLE_USER));
        memberRepository.registerMember(new Member("admin@email.com", bCryptEncryptor.encode("5678"), Role.ROLE_ADMIN));
    }
}

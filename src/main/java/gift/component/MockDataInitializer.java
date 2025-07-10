package gift.component;

import gift.domain.Member;
import gift.domain.WishList;
import gift.enums.Role;
import gift.repository.MemberRepository;
import gift.repository.WishListRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MockDataInitializer implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final WishListRepository wishListRepository;
    private final BCryptEncryptor bCryptEncryptor;

    public MockDataInitializer(MemberRepository memberRepository, WishListRepository wishListRepository, BCryptEncryptor bCryptEncryptor) {
        this.memberRepository = memberRepository;
        this.wishListRepository = wishListRepository;
        this.bCryptEncryptor = bCryptEncryptor;
    }

    @Override
    public void run(String... args) throws Exception {
        memberRepository.registerMember(new Member("test1@email.com", bCryptEncryptor.encode("1234"), Role.ROLE_USER));
        memberRepository.registerMember(new Member("test2@email.com", bCryptEncryptor.encode("1234"), Role.ROLE_USER));
        memberRepository.registerMember(new Member("test3@email.com", bCryptEncryptor.encode("1234"), Role.ROLE_USER));
        memberRepository.registerMember(new Member("admin@email.com", bCryptEncryptor.encode("5678"), Role.ROLE_ADMIN));

        wishListRepository.save(new WishList(1L, 2L));
        wishListRepository.save(new WishList(1L, 2L));
        wishListRepository.save(new WishList(1L, 3L));
        wishListRepository.save(new WishList(1L, 1L));
        wishListRepository.save(new WishList(1L, 1L));
        wishListRepository.save(new WishList(1L, 1L));

        wishListRepository.save(new WishList(2L, 1L));
        wishListRepository.save(new WishList(2L, 3L));
    }
}

package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Member;
import gift.entity.Role;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;

@DataJdbcTest
@Import(MemberRepository.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 저장 및 이메일로 조회 테스트")
    void saveAndFindByEmail() {

        Member newMember = new Member(null, "test@example.com", "password123", Role.USER);

        memberRepository.save(newMember);
        Optional<Member> foundMemberOptional = memberRepository.findByEmail("test@example.com");

        assertThat(foundMemberOptional).isPresent();

        Member foundMember = foundMemberOptional.get();
        assertThat(foundMember.getEmail()).isEqualTo(newMember.getEmail());
        assertThat(foundMember.getRole()).isEqualTo(Role.USER);
    }
}
package gift.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import gift.domain.Member;

@JdbcTest
class MemberRepositoryTest {

    @Autowired
    private JdbcClient jdbcClient;

    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new MemberRepository(jdbcClient);

        jdbcClient.sql("DELETE FROM member").update();
        jdbcClient.sql("ALTER TABLE member ALTER COLUMN id RESTART WITH 1").update();

        Member member1 = new Member(null, "member1@test.com", "dbPassword1", "ROLE_USER");
        Member member2 = new Member(null, "member2@test.com", "dbPassword2", "ROLE_USER");

        memberRepository.save(member1);
        memberRepository.save(member2);
    } // given

    @Test
    void findAllTest() {
        // when
        List<Member> members = memberRepository.findAll();

        // then
        assertThat(members).hasSize(2);
        assertThat(members.get(0).getId()).isEqualTo(1L);
        assertThat(members.get(0).getEmail()).isEqualTo("member1@test.com");
        assertThat(members.get(1).getId()).isEqualTo(2L);
        assertThat(members.get(1).getEmail()).isEqualTo("member2@test.com");
    }

    @Test
    void findByIdTest() {
        // when
        Optional<Member> member = memberRepository.findById(1L);

        // then
        assertThat(member).isPresent();
        assertThat(member.get().getEmail()).isEqualTo("member1@test.com");
        assertThat(member.get().getPassword()).isEqualTo("dbPassword1");
        assertThat(member.get().getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    void findByIdEmptyTest() {
        // when
        Optional<Member> member = memberRepository.findById(999L);

        // then
        assertThat(member).isEmpty();
    }

    @Test
    void findByEmailTest() {
        // when
        Optional<Member> member = memberRepository.findByEmail("member1@test.com");

        // then
        assertThat(member).isPresent();
        assertThat(member.get().getId()).isEqualTo(1L);
        assertThat(member.get().getPassword()).isEqualTo("dbPassword1");
    }

    @Test
    void findByEmailEmptyTest() {
        // when
        Optional<Member> member = memberRepository.findByEmail("mymail@localhost:8080");

        // then
        assertThat(member).isEmpty();
    }

    @Test
    void existsByIdTest() {
        // when, then
        assertThat(memberRepository.existsById(1L)).isEqualTo(true);
        assertThat(memberRepository.existsById(999L)).isEqualTo(false);
    }

    @Test
    void existsByEmailTest() {
        // when, then
        assertThat(memberRepository.existsByEmail("member1@test.com")).isEqualTo(true);
        assertThat(memberRepository.existsByEmail("mymail@localhost:8080")).isEqualTo(false);
    }

    @Test
    void saveTest() {
        // given
        Member member = new Member(null, "newmember@test.com", "dbPassword3", "ROLE_USER");

        // when
        Long savedId = memberRepository.save(member);

        // then
        assertThat(savedId).isEqualTo(3L);

        Optional<Member> savedMember = memberRepository.findById(savedId);
        assertThat(savedMember).isPresent();
        assertThat(savedMember.get().getEmail()).isEqualTo("newmember@test.com");
        assertThat(savedMember.get().getPassword()).isEqualTo("dbPassword3");
    }

    @Test
    void updateTest() {
        // given
        Member member = new Member(1L, "updatedmember@test.com", "dbPassword1", "ROLE_USER");

        // when
        int updatedCount = memberRepository.update(member);

        // then
        assertThat(updatedCount).isEqualTo(1);

        Optional<Member> updatedMember = memberRepository.findById(1L);
        assertThat(updatedMember).isPresent();
        assertThat(updatedMember.get().getEmail()).isEqualTo("updatedmember@test.com");
        assertThat(updatedMember.get().getPassword()).isEqualTo("dbPassword1");
        assertThat(updatedMember.get().getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    void deleteTest() {
        // when
        int count = memberRepository.delete(1L);

        // then
        assertThat(count).isEqualTo(1);
        assertThat(memberRepository.existsById(1L)).isEqualTo(false);
    }
}

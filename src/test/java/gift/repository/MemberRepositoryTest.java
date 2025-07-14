package gift.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gift.entity.Member;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "test@test.com", "cGFzc3dvcmQxMjM=", "USER");
    }

    @Test
    void save_success() {
        Member newMember = new Member(null, "new@test.com", "cGFzc3dvcmQxMjM=", "USER");
        Member savedMember = memberRepository.save(newMember);

        assertNotNull(savedMember.getId());
        assertEquals("new@test.com", savedMember.getEmail());
        assertEquals("cGFzc3dvcmQxMjM=", savedMember.getPassword());
        assertEquals("USER", savedMember.getRole());
    }

    @Test
    void save_invalidField() {
        Member invalidMember = new Member(null, null, null, null);
        assertThrows(IllegalArgumentException.class, () -> memberRepository.save(invalidMember));
    }

    @Test
    void update_success() {
        Member updatedMember = new Member(1L, "updated@test.com", "newpass", "ADMIN");
        Member result = memberRepository.update(updatedMember);

        assertEquals("updated@test.com", result.getEmail());
        assertEquals("newpass", result.getPassword());
        assertEquals("ADMIN", result.getRole());
    }

    @Test
    void update_notFound() {
        Member invalidMember = new Member(999L, "nonexistent@test.com", "pass", "USER");
        assertThrows(IllegalArgumentException.class, () -> memberRepository.update(invalidMember));
    }

    @Test
    void findById_success() {
        Optional<Member> foundMember = memberRepository.findById(1L);
        assertTrue(foundMember.isPresent());
        assertEquals("test@test.com", foundMember.get().getEmail());
    }

    @Test
    void findById_notFound() {
        Optional<Member> foundMember = memberRepository.findById(999L);
        assertTrue(foundMember.isEmpty());
    }

    @Test
    void findByEmail_success() {
        Optional<Member> foundMember = memberRepository.findByEmail("test@test.com");
        assertTrue(foundMember.isPresent());
        assertEquals(1L, foundMember.get().getId());
    }

    @Test
    void findByEmail_notFound() {
        Optional<Member> foundMember = memberRepository.findByEmail("nonexistent@test.com");
        assertTrue(foundMember.isEmpty());
    }

    @Test
    void delete_success() {
        assertDoesNotThrow(() -> memberRepository.delete("remove@test.com"));
        Optional<Member> deletedMember = memberRepository.findByEmail("remove@test.com");
        assertTrue(deletedMember.isEmpty());
    }

    @Test
    void delete_notFound() {
        assertThrows(IllegalArgumentException.class,
            () -> memberRepository.delete("nonexistent@test.com"));
    }

    @Test
    void findAll_success() {
        List<Member> members = memberRepository.findAll();
        assertFalse(members.isEmpty());
        assertEquals(2, members.size());
        assertEquals("test@test.com", members.get(0).getEmail());
    }
}
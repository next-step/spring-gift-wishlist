package gift.repository;

import gift.domain.Member;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepository {
    private final JdbcClient jdbc;

    public MemberRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public void save(Member member) {
        jdbc.sql("INSERT INTO member (email, password) VALUES (:email, :password)")
                .param("email", member.getEmail())
                .param("password", member.getPassword())
                .update();
    }

    public Optional<Member> findByEmail(String email) {
        return jdbc.sql("SELECT * FROM member WHERE email = :email")
                .param("email", email)
                .query(Member.class).optional();
    }
}

package gift.member.repository;

import gift.member.entity.Member;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void save(Member member) {
        jdbcClient.sql(
                        "INSERT INTO member (uuid, email, password, name, created_at, updated_at) "
                                + "VALUES (?, ?, ?, ?, ?, ?)")
                .param(member.getUuid())
                .param(member.getEmail())
                .param(member.getPassword())
                .param(member.getName())
                .param(member.getCreatedAt())
                .param(member.getUpdatedAt())
                .update();
    }

    public Optional<Member> findByEmail(String email) {
        return jdbcClient.sql("SELECT * FROM member WHERE email = ?")
                .param(email)
                .query(Member.class)
                .optional();
    }

    public boolean existsByEmail(String email) {
        return jdbcClient.sql("SELECT EXISTS(SELECT 1 FROM member WHERE email = ?)")
                .param(email)
                .query(Boolean.class)
                .single();
    }

    public Optional<Member> findByUuid(UUID uuid) {
        return jdbcClient.sql("SELECT * FROM member WHERE uuid = ?")
                .param(uuid.toString())
                .query(Member.class)
                .optional();
    }
}

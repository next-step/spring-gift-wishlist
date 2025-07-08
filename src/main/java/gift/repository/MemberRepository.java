package gift.repository;

import gift.entity.Member;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbcClient.sql("SELECT COUNT(*) FROM members WHERE email = :email")
                .param("email", email)
                .query(Integer.class)
                .single();
        return count > 0;
    }

    public void save(Member member) {
        jdbcClient.sql("INSERT INTO members (email, password) VALUES (:email, :password)")
                .param("email", member.getEmail())
                .param("password", member.getPassword())
                .update();

        Member saved = findByEmail(member.getEmail()).orElseThrow();
        member.setId(saved.getId());
    }


    public Optional<Member> findByEmail(String email) {
        return jdbcClient.sql("SELECT * FROM members WHERE email = :email")
                .param("email", email)
                .query((rs, rowNum) -> new Member(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password")
                ))
                .optional();
    }
}

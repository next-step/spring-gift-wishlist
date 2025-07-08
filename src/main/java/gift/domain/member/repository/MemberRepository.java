package gift.domain.member.repository;

import gift.domain.member.Member;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepository {
    private final JdbcClient jdbcClient;

    public MemberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = :email";
        return jdbcClient.sql(sql)
            .param("email", email)
            .query(Member.class)
            .optional();
    }

    public Optional<Member> save(Member member) {
        String insertSql = "INSERT INTO member (email, password, name, role) VALUES (:email, :password, :name, :role)";
        jdbcClient.sql(insertSql)
                .param("email", member.getEmail())
                .param("password", member.getPassword())
                .param("name", member.getName())
                .param("role", member.getRole())
                .update();
        return Optional.of(member);
    }
}

package gift.repository;

import gift.entity.Member;
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
        return jdbcClient.sql("SELECT id, email, password, role FROM member WHERE email = ?\n")
                .param(email)
                .query((rs, rowNum) ->
                        new Member(
                                rs.getLong("id"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("role")
                        )
                )
                .optional();
    }

    public void save(Member member) {
        jdbcClient.sql("INSERT INTO member (email, password, role) VALUES (?, ?, ?)")
                .params(member.getEmail(), member.getPassword(), member.getRole())
                .update();
    }

    public Optional<Member> findById(Long id) {
        return jdbcClient.sql("SELECT id, email, password, role FROM member WHERE id = ?")
                .param(id)
                .query((rs, rowNum) ->
                        new Member(
                                rs.getLong("id"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("role")
                        )
                )
                .optional();
    }

}

package gift.repository;

import gift.entity.Member;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    private final JdbcClient jdbcClient;

    public MemberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Member registerMember(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
                INSERT INTO MEMBERS (email, password)
                VALUES (:email, :password)
                """;

        jdbcClient.sql(sql)
                .param("email", member.getEmail())
                .param("password", member.getPassword())
                .update(keyHolder, "id");

        Long id = keyHolder.getKeyAs(Long.class);
        member.setId(id);
        return member;
    }

    public Optional<Member> findMemberByEmail(String email) {
        String sql = """
            SELECT * FROM MEMBERS WHERE email = :email
            """;

        return jdbcClient.sql(sql)
                .param("email", email)
                .query(memberRowMapper())
                .optional();
    }

    private RowMapper<Member> memberRowMapper() {
        return new RowMapper<Member>() {
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String email = rs.getString("email");
                String password = rs.getString("password");
                return new Member(id, email, password);
            }
        };
    }

}

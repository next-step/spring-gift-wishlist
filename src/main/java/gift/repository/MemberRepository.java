package gift.repository;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import gift.entity.Member;
import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcClient jdbcClient;

    MemberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static final String SAVE_MEMBER = """
            INSERT INTO members(email, password) VALUES
            (:email, :password);
            """;
    private static final String FIND_MEMBER_BY_EMAIL = """
            SELECT id, email, password
            FROM members
            WHERE email = :email;
            """;

    public Optional<Number> saveMember(String email, String password) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(SAVE_MEMBER)
                .param("email", email)
                .param("password", password)
                .update(keyHolder);
        return Optional.ofNullable(keyHolder.getKey());
    }

    public Optional<Member> findMemberByEmail(String email) {
        return jdbcClient.sql(FIND_MEMBER_BY_EMAIL)
                .param("email", email)
                .query((rs, numOfRows) ->
                        new Member(
                                rs.getLong("id"),
                                rs.getString("email"),
                                rs.getString("password")
                        )).optional();
    }
}

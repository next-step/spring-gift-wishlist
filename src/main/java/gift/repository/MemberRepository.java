package gift.repository;

import gift.entity.Member;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Member save(Member member) {
        String sql="INSERT INTO member(email,password) VALUES(:email,:password)";
        jdbcClient.sql(sql)
                .param("email", member.getEmail())
                .param("password", member.getPassword())
                .update();


        return member;
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = :email";

        return jdbcClient.sql(sql)
                .param("email", email)
                .query(MEMBER_ROW_MAPPER)
                .optional();
    }

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (rs, rowNum) ->
            new Member(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password")
            );

}

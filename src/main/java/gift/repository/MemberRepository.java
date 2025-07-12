package gift.repository;

import gift.entity.Member;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Member save(Member member) {
        String sql="INSERT INTO member(email,password,role) VALUES(:email,:password,:role)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .param("email", member.getEmail())
                .param("password", member.getPassword())
                .param("role", member.getRole())
                .update(keyHolder);

        // 생성된 키(ID) 추출
        Number key = keyHolder.getKey();
        if (key != null) {
            member.setId(key.longValue());
        }

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
                    rs.getString("password"),
                    rs.getString("role")
            );


    public Optional<Member> findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = :id";

        return jdbcClient.sql(sql)
                .param("id", id)
                .query(MEMBER_ROW_MAPPER)
                .optional();
    }
}

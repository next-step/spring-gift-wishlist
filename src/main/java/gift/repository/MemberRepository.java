package gift.repository;

import gift.entity.Member;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member save(Member member) {

        String sql = "insert into member (email, password) values (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, member.getEmail());
            ps.setString(2, member.getPassword());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();

        if (key != null) {
            return member.withId(key.longValue(), member.getEmail(), member.getPassword());
        }

        return member;
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "select * from member where email = ?";

        return jdbcTemplate.query(sql, MEMBER_ROW_MAPPER, email)
                .stream()
                .findFirst();
    }

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (rs, rowNum) -> Member.createMember(
            rs.getString("email"),
            rs.getString("password")
    );
}

package gift.repository;

import gift.entity.Member;
import gift.entity.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveMember(Member member) {
        String sql = "INSERT INTO member (email, password, role) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getRole().name());
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        return jdbcTemplate.query(sql, new Object[]{email}, memberRowMapper())
                .stream().findFirst();
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> new Member(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
                Role.valueOf(rs.getString("role"))
        );
    }
}

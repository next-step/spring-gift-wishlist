package gift.repository;

import gift.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member save(Member member) {
        String sql = "INSERT INTO members (email, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword());

        return findByEmail(member.getEmail()).orElseThrow();
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM members WHERE email = ?";
        return jdbcTemplate.query(sql, memberRowMapper(), email).stream().findFirst();
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> new Member(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password")
        );
    }
}
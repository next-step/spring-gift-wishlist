package gift.member.repository;

import gift.member.entity.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, email, password FROM members WHERE email = ?";
        try {
            Member member = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{email},
                    (rs, rowNum) -> new Member(
                            rs.getLong("id"),
                            rs.getString("email"),
                            rs.getString("password")
                    )
            );
            return Optional.of(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void save(Member member) {
        String sql = "INSERT INTO members (email, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword());
    }

    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, email, password FROM members WHERE id = ?";
        try {
            Member member = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{id},
                    (rs, rowNum) -> new Member(
                            rs.getLong("id"),
                            rs.getString("email"),
                            rs.getString("password")
                    )
            );
            return Optional.of(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

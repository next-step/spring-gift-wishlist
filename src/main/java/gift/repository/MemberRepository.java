package gift.repository;

import gift.entity.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository("MemberRepository")
public class MemberRepository implements MemberRepositoryInterface {
    private JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, this::mapRowToMember, email);
            return Optional.ofNullable(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Member member) {
        String sql = "INSERT INTO member (email, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword());
    }

    private Member mapRowToMember(ResultSet rs, int rowNum) throws SQLException {
        Member member = new Member(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password")
        );
        return member;
    }
}

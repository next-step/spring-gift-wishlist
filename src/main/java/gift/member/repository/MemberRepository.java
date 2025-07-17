package gift.member.repository;

import gift.member.entity.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Member> memberRowMapper = (rs, rowNum) -> {
        Member m = new Member();
        m.setId(rs.getLong("id"));
        m.setEmail(rs.getString("email"));
        m.setPassword(rs.getString("password"));
        m.setRole(rs.getString("role"));
        return m;
    };

    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, email, password, role FROM members WHERE email = ?";
        List<Member> result = jdbcTemplate.query(sql, memberRowMapper, email);
        return result.stream().findFirst();
    }

    public Member save(Member member) {
        String sql = "INSERT INTO members (email, password, role) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, member.getEmail());
            ps.setString(2, member.getPassword());
            ps.setString(3, member.getRole());
            return ps;
        }, keyHolder);
        member.setId(keyHolder.getKey().longValue());
        return member;
    }

    public void update(Member member) {
        String sql = "UPDATE members SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getId());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM members WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Member> findAll() {

        String sql = "SELECT id, email, password, role FROM members";

        return jdbcTemplate.query(sql, memberRowMapper);
    }

    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, email, password, role FROM members WHERE id = ?";
        List<Member> result = jdbcTemplate.query(sql, memberRowMapper, id);
        return result.stream().findFirst();
    }
}

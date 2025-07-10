package gift.repository;

import gift.entity.Member;
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

    private JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) -> {
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setEmail(rs.getString("email"));
        member.setPassword(rs.getString("password"));
        return member;
    };

    public Member save(Member member) {
        String sql = "INSERT INTO member (email, password) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, member.getEmail());
            ps.setString(2, member.getPassword());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        member.setId(key != null ? key.longValue() : null);
        return member;
    }

    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, email, password FROM member WHERE id = ?";
        List<Member> results = jdbcTemplate.query(sql, memberRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, email, password FROM member WHERE email = ?";
        List<Member> results = jdbcTemplate.query(sql, memberRowMapper, email);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<Member> findAll() {
        String sql = "SELECT id, email, password FROM member ORDER BY id";
        return jdbcTemplate.query(sql, memberRowMapper);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM member WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public void update(Long id, Member member) {
        String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), id);
    }
} 
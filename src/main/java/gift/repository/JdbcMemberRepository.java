package gift.repository;

import gift.entity.Member;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Member save(Member member) {
        String sql = "INSERT INTO members (email, password) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, member.getEmail());
            ps.setString(2, member.getPassword());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();
        return member.assignId(generatedId);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, email, password FROM members WHERE email = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql,
                    new BeanPropertyRowMapper<>(Member.class), email);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, email, password FROM members WHERE id = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql,
                    new BeanPropertyRowMapper<>(Member.class), id);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT id, email, password FROM members";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Member.class));
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM members WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

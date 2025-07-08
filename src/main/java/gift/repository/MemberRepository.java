package gift.repository;

import gift.entity.Member;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password")
    );

    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, email, password FROM member WHERE email = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, memberRowMapper, email);
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Member save(Member member) {
        String sql = "INSERT INTO member (email, password) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, member.getEmail());
            ps.setString(2, member.getPassword());
            return ps;
        }, keyHolder);
        member.setId(keyHolder.getKey().longValue());
        return member;
    }
}

package gift.repository.member;

import gift.entity.member.Member;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Member> rowMapper = (rs, rowNum) -> Member.of(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password_hash"),
            rs.getString("role"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    public MemberRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, email, password_hash, role, created_at FROM members WHERE email = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, rowMapper, email);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Member save(Member member) {
        String sql = "INSERT INTO members(email, password_hash, role, created_at) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, member.getEmail().email());
            ps.setString(2, member.getPasswordHash().password());
            ps.setString(3, member.getRole().name());
            ps.setTimestamp(4, Timestamp.valueOf(member.getCreatedAt()));
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return member.withId(key.longValue());
    }

    @Override
    public int update(Member member) {
        String sql = "UPDATE members SET email = ?, password_hash = ?, role = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                member.getEmail().email(),
                member.getPasswordHash().password(),
                member.getRole().name(),
                member.getId().id()
        );
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM members WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}

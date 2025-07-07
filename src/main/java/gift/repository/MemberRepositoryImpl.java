package gift.repository;

import gift.entity.Member;
import gift.exception.FailedGenerateKeyException;
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
public class MemberRepositoryImpl implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemberRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Member registerMember(Member member) {
        final String sql = "INSERT INTO members (email, password) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, member.getEmail());
            ps.setString(2, member.getPassword());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new FailedGenerateKeyException();
        }

        return new Member(
                key.longValue(),
                member.getEmail(),
                member.getPassword());
    }

    @Override
    public Optional<Member> findMemberByEmail(String email) {
        final String sql = "SELECT * FROM members WHERE email = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberRowMapper(), email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password")
        );
    }
}

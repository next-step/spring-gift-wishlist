package gift.member.repository;

import gift.member.entity.Member;
import gift.global.exception.FailedGenerateKeyException;
import gift.global.exception.MemberNotFoundException;
import gift.member.vo.Email;
import gift.member.vo.Name;
import gift.member.vo.Password;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class MemberRepositoryImpl implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemberRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Member save(Member member) {
        final String sql = "INSERT INTO members (name, email, password) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(
                1,
                member.getName()
                    .getValue()
            );
            ps.setString(
                2,
                member.getEmail()
                    .getValue()
            );
            ps.setString(
                3,
                member.getPassword()
                    .getValue());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new FailedGenerateKeyException();
        }

        return new Member(
                key.longValue(),
                member.getName(),
                member.getEmail(),
                member.getPassword());
    }

    @Override
    public Member findByEmail(String email) {
        final String sql = "SELECT * FROM members WHERE email = ?";

        try {
            return jdbcTemplate.queryForObject(sql, memberRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Member> findAll() {
        final String sql = "SELECT * FROM members";

        return jdbcTemplate.query(sql, memberRowMapper());
    }

    @Override
    public Member findById(Long id) {
        final String sql = "SELECT * FROM members WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, memberRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new MemberNotFoundException();
        }
    }

    @Override
    public void update(Member member) {
        final String sql = "UPDATE members SET name = ?, email = ?, password = ? WHERE id = ?";

        int updated = jdbcTemplate.update(
            sql,
            member.getName()
                .getValue(),
            member.getEmail()
                .getValue(),
            member.getPassword()
                .getValue(),
            member.getId()
        );
        if (updated == 0) {
            throw new MemberNotFoundException();
        }
    }

    @Override
    public void delete(Long id) {
        final String sql = "DELETE FROM members WHERE id = ?";

        int deleted = jdbcTemplate.update(sql, id);
        if (deleted == 0) {
            throw new MemberNotFoundException();
        }
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> new Member(
            rs.getLong("id"),
            new Name(rs.getString("name")),
            new Email(rs.getString("email")),
            new Password(rs.getString("password"))
        );
    }
}

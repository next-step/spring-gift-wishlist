package gift.repository;

import gift.domain.Member;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
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
        Objects.requireNonNull(member, "member는 null일 수 없습니다.");

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO member (email, password) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, member.email());
            ps.setString(2, member.password());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("ID 생성에 실패했습니다.");
        }

        Long generatedId = key.longValue();
        return new Member(generatedId, member.email(), member.password());
    }

    @Override
    public void update(Long id, Member updatedMember) {
        Objects.requireNonNull(id, "id는 null일 수 없습니다.");
        Objects.requireNonNull(updatedMember, "updatedMember는 null일 수 없습니다.");

        int updatedRows = jdbcTemplate.update(
                "UPDATE member SET email = ?, password = ? WHERE id = ?",
                updatedMember.email(),
                updatedMember.password(),
                id
        );

        if (updatedRows == 0) {
            throw new IllegalArgumentException("해당 ID의 회원이 존재하지 않아 업데이트할 수 없습니다: " + id);
        }
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query(
                "SELECT id, email, password FROM member",
                this::mapRowToMember
        );
    }

    @Override
    public Optional<Member> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        try {
            Member member = jdbcTemplate.queryForObject(
                    "SELECT id, email, password FROM member WHERE id = ?",
                    this::mapRowToMember,
                    id
            );
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }

        try {
            Member member = jdbcTemplate.queryForObject(
                    "SELECT id, email, password FROM member WHERE email = ?",
                    this::mapRowToMember,
                    email
            );
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    private Member mapRowToMember(ResultSet rs, int rowNum) throws java.sql.SQLException {
        return Member.withId(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password")
        );
    }
}

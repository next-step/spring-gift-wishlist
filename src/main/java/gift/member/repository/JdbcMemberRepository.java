package gift.member.repository;

import gift.member.Member;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleInsert;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("member")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, name, email, password FROM member WHERE email = ?";

        try {
            Member member = jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Member(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
                ), email
            );
            return Optional.ofNullable(member);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, name, email, password FROM member WHERE id = ?";

        try {
            Member member = jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Member(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
                ), id
            );
            return Optional.ofNullable(member);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT id, name, email, password FROM member";
        return jdbcTemplate.query(sql,
            (rs, rowNum) -> new Member(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password")
            )
        );
    }

    @Override
    public Member save(Member member) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", member.getName());
        params.put("email", member.getEmail());
        params.put("password", member.getPassword());

        Long generatedValue = simpleInsert.executeAndReturnKey(params).longValue();
        member.setId(generatedValue);

        return member;
    }

    @Override
    public Member updateProfile(Member member) {
        String sql = "UPDATE member SET name = ?, email = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getName(), member.getEmail(), member.getId());
        return member;
    }

    @Override
    public void updatePassword(Member member) {
        String sql = "UPDATE member SET password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getPassword(), member.getId());
    }

    @Override
    public void remove(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

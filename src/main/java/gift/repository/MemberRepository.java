package gift.repository;

import gift.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<Member> rowMapper = (rs, rowNum) ->
            new Member(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password")
            );

    public MemberRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Member save(Member member) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", member.getEmail());
        params.put("password", member.getPassword());

        Number key = jdbcInsert.executeAndReturnKey(params);
        return new Member(key.longValue(), member.getEmail(), member.getPassword());
    }

    public Optional<Member> findByEmail(String email) {
        List<Member> results = jdbcTemplate.query(
                "SELECT * FROM member WHERE email = ?",
                rowMapper,
                email
        );
        return results.stream().findFirst();
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM member WHERE email = ?",
                Integer.class,
                email
        );
        return count != null && count > 0;
    }

    public Optional<Member> findById(Long id) {
        List<Member> results = jdbcTemplate.query(
                "SELECT * FROM member WHERE id = ?",
                rowMapper,
                id
        );
        return results.stream().findFirst();
    }

    public List<Member> findAll() {
        return jdbcTemplate.query("SELECT * FROM member", rowMapper);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM member WHERE id = ?", id);
    }
}

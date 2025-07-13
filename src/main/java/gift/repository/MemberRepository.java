package gift.repository;

import gift.entity.Member;
import gift.entity.Role;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberRepository(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("members")
            .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) ->
        new Member(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password"),
            Role.valueOf(rs.getString("role"))
        );

    public Member save(Member member) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", member.getEmail());
        params.put("password", member.getPassword());
        params.put("role", member.getRole().name());

        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Member(id, member.getEmail(), member.getPassword(), member.getRole());
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, email, password, role FROM members WHERE email = :email";
        return jdbcClient.sql(sql)
            .param("email", email)
            .query(memberRowMapper)
            .optional();
    }
}
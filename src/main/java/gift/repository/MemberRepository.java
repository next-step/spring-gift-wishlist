package gift.repository;

import gift.entity.Member;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;


    private final RowMapper<Member> memberRowMapper = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password")
    );

    public MemberRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Member save(Member member) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword());

        Number key = jdbcInsert.executeAndReturnKey(params);
        member.setId(key.longValue());
        return member;
    }


    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, email, password FROM member WHERE email = :email";

        MapSqlParameterSource param = new MapSqlParameterSource("email", email);

        List<Member> members = template.query(sql, param, memberRowMapper);

        return members.stream().findFirst();
    }
}
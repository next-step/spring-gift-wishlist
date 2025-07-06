package gift.repository;

import gift.entity.Member;
import gift.enums.Role;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MemberJdbcRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemberJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void registerMember(String email, String password) {
        String sql = "INSERT INTO member (email, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, email, password);
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = """
                SELECT COUNT(*)
                FROM member
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);

        return count != null && count > 0;
    }

    @Override
    public Member findMemberByEmailOrElseThrow(String email) {
        String sql = """
                SELECT *
                FROM member
                WHERE email = ?
                """;
        List<Member> result = jdbcTemplate.query(sql, memberRowMapper(), email);

        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 이메일을 찾을 수 없습니다."));
    }

    private RowMapper<Member> memberRowMapper() {
        return new RowMapper<Member>() {
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Member(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role")),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                );
            }
        };
    }
}

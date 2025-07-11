package gift.repository;

import gift.domain.Member;
import gift.enums.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJdbcRepository implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemberJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void registerMember(Member member) {
        String sql = "INSERT INTO member (email, password, role) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getRole().name());
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
    public Optional<Member> findMemberByEmail(String email) {
        String sql = """
                SELECT *
                FROM member
                WHERE email = ?
                """;
        List<Member> result = jdbcTemplate.query(sql, memberRowMapper(), email);

        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findMemberById(Long id) {
        String sql = """
                SELECT *
                FROM member
                WHERE id = ?
                """;
        List<Member> result = jdbcTemplate.query(sql, memberRowMapper(), id);

        return result.stream().findAny();
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

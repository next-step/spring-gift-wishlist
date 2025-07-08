package gift.repository;

import gift.entity.Member;
import gift.entity.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "select * from members where email = ?";
        List<Member> members = jdbcTemplate.query(sql, (rs, rn) ->
                new Member(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role"))
                ), email);

        return members.isEmpty() ? Optional.empty() : Optional.of(members.getFirst());
    }

    public Member register(Member member) {
        String sql = "insert into members (email, password, role) values (?, ?, ?)";

        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), "USER");

        return new Member(0, member.getEmail(), member.getPassword(), member.getRole());
    }
}

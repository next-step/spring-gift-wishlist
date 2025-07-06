package gift.repository;

import gift.entity.Member;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
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
        List<Member> members = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Member.class), email);
        return Optional.ofNullable(members.isEmpty() ? null : members.getFirst());
    }

    public Member save(Member member) {
        String sql = "insert into members (name, email, password) values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update( connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPassword());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            return new Member(key.longValue(), member.getName(), member.getEmail(), member.getPassword());
        }
        return member;
    }

    public List<Member> findAll() {
        String sql = "select * from members";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Member.class));
    }
}

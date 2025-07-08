package gift.repository;

import gift.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {
    private final JdbcTemplate jdbc;

    public MemberRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Member> ROW_MAPPER = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password")
    );

    public Optional<Member> findByEmail(String email) {
        List<Member> list = jdbc.query("SELECT * FROM members WHERE email = ?", ROW_MAPPER, email);
        return list.stream().findFirst();
    }

    public Long save(Member member) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO members (email, password) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, member.getEmail());
            ps.setString(2, member.getPassword());
            return ps;
        }, kh);
        return kh.getKey().longValue();
    }
}

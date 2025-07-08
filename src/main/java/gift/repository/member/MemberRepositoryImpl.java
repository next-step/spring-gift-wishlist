package gift.repository.member;

import gift.entity.member.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final DataSource ds;
    private final SimpleJdbcInsert insert;

    public MemberRepositoryImpl(DataSource ds) {
        this.ds = ds;
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName("members")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(Member member) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", member.getEmail().email());
        params.put("password_hash", member.getPasswordHash().password());
        params.put("role", member.getRole().name());
        params.put("created_at", Timestamp.valueOf(member.getCreatedAt()));

        try {
            Number newId = insert.executeAndReturnKey(params);
            return member.withId(newId.longValue());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.", e);
        }
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, email, password_hash, role, created_at FROM members WHERE email = ?";
        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(Member.of(
                            rs.getLong("id"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getString("role"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    ));
                }
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

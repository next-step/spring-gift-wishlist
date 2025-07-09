package gift.repository.member;

import gift.entity.member.Member;
import gift.exception.DuplicateEmailException;
import gift.exception.MemberNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public Member register(Member member) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", member.getEmail().email());
        params.put("password_hash", member.getPassword().password());
        params.put("role", member.getRole().name());
        params.put("created_at", Timestamp.valueOf(member.getCreatedAt()));

        try {
            Number newId = insert.executeAndReturnKey(params);
            return member.withId(newId.longValue());
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException();
        }
    }

    @Override
    public Member updateMember(Member member) {
        String sql = """
                UPDATE members
                   SET email = ?, password_hash = ?, role = ?
                 WHERE id = ?
                """;
        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, member.getEmail().email());
            ps.setString(2, member.getPassword().password());
            ps.setString(3, member.getRole().name());
            ps.setLong(4, member.getId().id());
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new MemberNotFoundException(member.getId().toString());
            }
            return member;
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT id, email, password_hash, role, created_at FROM members";
        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            List<Member> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, email, password_hash, role, created_at FROM members WHERE id = ?";
        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM members WHERE id = ?";
        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Member mapRow(ResultSet rs) throws Exception {
        return Member.of(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("role"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}

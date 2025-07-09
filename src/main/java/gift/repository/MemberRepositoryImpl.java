package gift.repository;

import gift.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcClient jdbcClient;

    public MemberRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (rs, rowNum) -> new Member(
        rs.getLong("id"),
        rs.getString("email"),
        rs.getString("password"),
        rs.getString("role")
    );

    @Override
    public Member save(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("member cannot be null");
        }

        if (member.getEmail() == null || member.getPassword() == null || member.getRole() == null) {
            throw new IllegalArgumentException("Required fields are missing");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO members (email, password, role) VALUES (?, ?, ?)";
        jdbcClient.sql(sql)
            .param(1, member.getEmail())
            .param(2, member.getPassword())
            .param(3, member.getRole())
            .update(keyHolder, new String[]{"id"});

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalArgumentException("Failed to get id");
        }
        Long id = key.longValue();

        return new Member(
            id,
            member.getEmail(),
            member.getPassword(),
            member.getRole()
        );
    }

    @Override
    public Member update(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("member cannot be null");
        }

        if (member.getId() == null || member.getEmail() == null || member.getPassword() == null
            || member.getRole() == null) {
            throw new IllegalArgumentException("Required fields are missing");
        }

        String sql = "UPDATE members SET email = ?, password = ?, role = ? WHERE id = ?";
        int rowsCount = jdbcClient.sql(sql)
            .param(1, member.getEmail())
            .param(2, member.getPassword())
            .param(3, member.getRole())
            .param(4, member.getId())
            .update();
        if (rowsCount == 0) {
            throw new IllegalArgumentException("Member(id: " + member.getId() + ") not found");
        }

        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, email, password, role FROM members WHERE id = ?";
        return jdbcClient.sql(sql)
            .param(1, id)
            .query(MEMBER_ROW_MAPPER)
            .optional();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, email, password, role FROM members WHERE email = ?";
        return jdbcClient.sql(sql)
            .param(1, email)
            .query(MEMBER_ROW_MAPPER)
            .optional();
    }

    @Override
    public void delete(String email) {
        String sql = "DELETE FROM members WHERE email = ?";
        int rowsCount = jdbcClient.sql(sql)
            .param(1, email)
            .update();
        if (rowsCount == 0) {
            throw new IllegalArgumentException("Member(email: " + email + " ) not found");
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT id, email, password, role FROM members";
        return jdbcClient.sql(sql)
            .query(MEMBER_ROW_MAPPER)
            .list();
    }

}

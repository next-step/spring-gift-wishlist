package gift.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import gift.domain.Member;

@Repository
public class MemberRepository {

    private final JdbcClient jdbcClient;
    private final RowMapper<Member> rowMapper;

    public MemberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
        this.rowMapper = new BeanPropertyRowMapper<>(Member.class);
    }

    public List<Member> findAll() {
         String sql = "SELECT * FROM member";

        return jdbcClient.sql(sql)
            .query(rowMapper)
            .list();
    }

    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, email, password FROM member WHERE id = :id";

        return jdbcClient.sql(sql)
            .param("id", id)
            .query(rowMapper)
            .optional();
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, email, password FROM member WHERE email = :email";

        return jdbcClient.sql(sql)
            .param("email", email)
            .query(rowMapper)
            .optional();
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM member WHERE id = :id";

        return jdbcClient.sql(sql)
            .param("id", id)
            .query(Long.class)
            .single() > 0;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM member WHERE email = :email";

        return jdbcClient.sql(sql)
            .param("email", email)
            .query(Long.class)
            .single() > 0;
    }

    public Long save(Member member) {
        String sql = "INSERT INTO member (email, password) VALUES (:email, :password)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
            .param("email", member.getEmail())
            .param("password", member.getPassword())
            .update(keyHolder);

        if (keyHolder.getKey() == null) {
            return -1L;
        }

        return keyHolder.getKey().longValue();
    }

    public int update(Member member) {
        String sql = """
            UPDATE member
            SET email = :email
            WHERE id = :id
            """;

        return jdbcClient.sql(sql)
            .param("email", member.getEmail())
            .param("id", member.getId())
            .update();
    }

    public int delete(Long id) {
        String sql = "DELETE FROM member WHERE id = :id";

        return jdbcClient.sql(sql)
            .param("id", id)
            .update();
    }
}

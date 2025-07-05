package gift.repository;

import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import gift.domain.User;

@Repository
public class AuthRepository {

    private final JdbcClient jdbcClient;

    public AuthRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    private static final RowMapper<User> rowMapper = (rs, rowNum) -> User.of(
        rs.getLong("id"),
        rs.getString("email"),
        rs.getString("password")
    );

    public Optional<User> findById(Long id) {
        String sql = "SELECT id, email, password FROM `user` WHERE id = :id";

        return jdbcClient.sql(sql)
            .param("id", id)
            .query(rowMapper)
            .optional();
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, email, password FROM `user` WHERE email = :email";

        return jdbcClient.sql(sql)
            .param("email", email)
            .query(rowMapper)
            .optional();
    }

    public Long save(User user) {
        String sql = "INSERT INTO `user` (email, password) VALUES (:email, :password)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
            .param("email", user.getEmail())
            .param("password", user.getPassword())
            .update(keyHolder);

        if (keyHolder.getKey() == null) {
            return -1L;
        }

        return keyHolder.getKey().longValue();
    }
}

package gift.repository;

import gift.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AuthRepository {
    private JdbcTemplate jdbcTemplate;
    private final AtomicLong id = new AtomicLong(0);

    public AuthRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(User user) {
        String sql = "INSERT INTO USERS(ID, EMAIL, PASSWORD) VALUES(?, ?, ?);";
        Object[] args = new Object[]{
            id.getAndIncrement(),
            user.getEmail(),
            user.getPassword()
        };
        int[] argTypes = {Types.BIGINT, Types.VARCHAR, Types.VARCHAR};
        jdbcTemplate.update(sql, args, argTypes);
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM USERS WHERE EMAIL = ?;";
        Object[] args = new Object[]{email};
        int[] argTypes = {Types.VARCHAR};
        return jdbcTemplate.query(sql, args, argTypes, rs -> {
                if (rs.next()) {
                    User user = new User();
                    user.setEmail(rs.getString("EMAIL"));
                    user.setPassword(rs.getString("PASSWORD"));
                    return Optional.of(user);
                }
                return Optional.empty();
            }
        );
    }
}

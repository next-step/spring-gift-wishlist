package gift.repository;

import gift.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.Optional;

@Repository
public class AuthRepository {
    private JdbcTemplate jdbcTemplate;

    public AuthRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User save(User user) {
        String sql = "INSERT INTO USERS(EMAIL, PASSWORD) VALUES(?, ?);";
        Object[] args = new Object[]{
            user.getEmail(),
            user.getPassword()
        };
        int[] argTypes = {Types.VARCHAR, Types.VARCHAR};
        jdbcTemplate.update(sql, args, argTypes);
        return user;
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

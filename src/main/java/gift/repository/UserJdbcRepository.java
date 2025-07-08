package gift.repository;


import gift.entity.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<User> userRowMapper() {
        return new RowMapper<>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new User(
                    rs.getString("user_role"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        };
    }

    @Override
    public void createUser(User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate)
            .withTableName("users")
            .usingColumns("user_role", "email", "password");

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("user_role", user.userRole());
        parameters.put("email", user.email());
        parameters.put("password", user.password());

        jdbcInsert.withTableName("users").execute(parameters);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return jdbcTemplate.query("select * from users where email = ?",
            userRowMapper(),
            email).stream().findFirst();
    }
}

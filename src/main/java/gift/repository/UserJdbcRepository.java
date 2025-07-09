package gift.repository;


import gift.entity.User;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate)
            .withTableName("users")
            .usingColumns("user_role", "email", "password");
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(
            rs.getString("user_role"),
            rs.getString("email"),
            rs.getString("password")
        );
    }

    @Override
    public void createUser(User user) {

        Map<String, Object> parameters = Map.of(
            "user_role", user.userRole(),
            "email", user.email(),
            "password", user.password());

        jdbcInsert.execute(parameters);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        try {
            User user = jdbcTemplate.queryForObject("select * from users where email = ?",
                userRowMapper(),
                email);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}

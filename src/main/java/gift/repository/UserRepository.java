package gift.repository;

import gift.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(User user) {
        String sql = "insert into users (email, password) values (?, ?)";

        jdbcTemplate.update(sql, user.getEmail(), user.getPassword());
    }

    public Optional<User> findByEmail(String email) {
        String sql = "select * from users where email = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email);
        return users.stream().findFirst();
    }

}

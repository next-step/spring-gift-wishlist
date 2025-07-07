package gift.repository.userRepository;


import gift.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository{

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password")
            );
        }
    };

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User save(User user) {
        var sql = "INSERT INTO users (email,password) VALUES (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.email());
            ps.setString(2, user.password());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new User(id, user.email(), user.password());

    }

    @Override
    public List<User> getAllUsers() {
        var sql = "SELECT ID, EMAIL, PASSWORD FROM users";

        return jdbcTemplate.query(sql,userRowMapper);
    }

    @Override
    public List<User> findUserByEmail(String email) {
        var sql = "SELECT ID, EMAIL, PASSWORD FROM users WHERE EMAIL = ?";
        return jdbcTemplate.query(sql, new Object[]{email}, userRowMapper);

    }

    @Override
    public User findUserById(Long id) {
        var sql = "SELECT ID, EMAIL, PASSWORD FROM users WHERE ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, userRowMapper);
    }

    @Override
    public User updateUser(User findUser, String email, String password) {
        var sql = "UPDATE users SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, email, password, findUser.id());
        return new User(findUser.id(), email, password);
    }

    @Override
    public void deleteUser(User findUser) {
        var sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, findUser.id());
    }

}

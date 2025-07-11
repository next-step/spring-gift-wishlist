package gift.repository;

import gift.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    public UserRepository(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    public User createUser(User user) {
        final String sql = "INSERT INTO users(email, password) VALUES(?, ?)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email",    user.email())
                .addValue("password", user.password());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    sql,
                    new String[] { "id", "created_at" , "role"}  // 반환받고 싶은 컬럼 지정
            );
            ps.setString(1, user.email());
            ps.setString(2, user.password());
            return ps;
        }, keyHolder);

        Long id = (Long) keyHolder.getKeys().get("id");
        Timestamp ts = (Timestamp) keyHolder.getKeys().get("created_at");
        LocalDateTime createdAt = ts.toLocalDateTime();
        String role = (String) keyHolder.getKeys().get("role");

        // 생성된 id, createdAt, role 값을 포함해 User 반환
        return new User(id, user.email(), user.password(), createdAt, role);
    }

    public User checkUser(User user) {
        //못 찾거나 2개 이상 찾을 경우 예외 발생
        return jdbcTemplate.queryForObject("SELECT id, email, password, created_at, role FROM users WHERE email=? AND password=?", userRowMapper(), user.email(), user.password());
    }

    public List<User> findAllUsers() {
        return jdbcTemplate.query("SELECT id, email, password, created_at, role FROM users", userRowMapper());
    }

    public User findUserById(Long id) {
        return jdbcTemplate.queryForObject("SELECT id, email, password, created_at, role FROM users WHERE id=?", userRowMapper(), id);
    }

    public User findUserByEmail(String email) {
        return jdbcTemplate.queryForObject("SELECT id, email, password, created_at, role FROM users WHERE email=?", userRowMapper(), email);
    }

    public boolean deleteUser(Long id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id = ?", id) != 0;
    }

    public boolean updateUser(Long id, User user) {
        return jdbcTemplate.update("UPDATE users SET email = ?, password = ? WHERE id = ?", user.email(), user.password(), id) != 0;
    }

    // users 전체 조회용 RowMapper
    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getString("role")
        );
    }
}

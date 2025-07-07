package gift.user;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserDao {

    private final JdbcClient jdbcClient;

    public UserDao(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public User save(User user) {
        jdbcClient.sql("INSERT INTO USERS (id, email, password) VALUES (:id, :email, :password)")
                .param("id", user.getId())
                .param("email", user.getEmail())
                .param("password", user.getPassword())
                .update();

        return user;
    }

    public User findByEmail(String email) throws EmptyResultDataAccessException {
        return jdbcClient.sql("SELECT * FROM USERS WHERE email = :email")
                .param("email", email)
                .query(User.class)
                .single();
    }

    public void delete(UUID id) {
        jdbcClient.sql("DELETE FROM USERS WHERE id = :id")
                .param("id", id)
                .update();
    }
}

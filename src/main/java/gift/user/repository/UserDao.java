package gift.user.repository;

import gift.user.domain.User;
import gift.user.dto.UserPatchRequestDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserDao {

    private final JdbcClient jdbcClient;

    public UserDao(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public User save(User user) {
        jdbcClient.sql("INSERT INTO USERS (id, email, password, salt) VALUES (:id, :email, :password, :salt)")
                .param("id", user.getId())
                .param("email", user.getEmail())
                .param("password", user.getPassword())
                .param("salt", user.getSalt())
                .update();

        return user;
    }

    public List<User> findAll() {
        return jdbcClient.sql("SELECT * FROM USERS")
                .query(User.class)
                .list();
    }
    public Optional<User> findById(UUID id) {
        return jdbcClient.sql("SELECT * FROM USERS WHERE id = :id")
                .param("id", id)
                .query(User.class)
                .optional();
    }
    public Optional<User> findByEmail(String email) throws EmptyResultDataAccessException {
        return jdbcClient.sql("SELECT * FROM USERS WHERE email = :email")
                .param("email", email)
                .query(User.class)
                .optional();
    }
    public void updateEmail(UUID id, String email) {
        jdbcClient.sql("UPDATE USERS SET email = :email WHERE id = :id")
                .param("email", email)
                .param("id", id)
                .update();
    }
    public void updatePassword(UUID id, String password) {
        jdbcClient.sql("UPDATE USERS SET password = :password WHERE id = :id")
                .param("password", password)
                .param("id", id)
                .update();
    }

    public void delete(UUID id) {
        jdbcClient.sql("DELETE FROM USERS WHERE id = :id")
                .param("id", id)
                .update();
    }
}

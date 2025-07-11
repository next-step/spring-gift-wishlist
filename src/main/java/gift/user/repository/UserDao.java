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
        jdbcClient.sql("INSERT INTO USERS (id, email, password) VALUES (:id, :email, :password)")
                .param("id", user.getId())
                .param("email", user.getEmail())
                .param("password", user.getPassword())
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

    @Transactional
    public User update(UUID id, UserPatchRequestDto userPatchRequestDto) {
        if (userPatchRequestDto.getEmail() != null) {
            jdbcClient.sql("UPDATE USERS SET email = :email WHERE id = :id")
                    .param("email", userPatchRequestDto.getEmail())
                    .param("id", id)
                    .update();
        }
        if (userPatchRequestDto.getPassword() != null) {
            jdbcClient.sql("UPDATE USERS SET password = :password WHERE id = :id")
                    .param("password", userPatchRequestDto.getPassword())
                    .param("id", id)
                    .update();
        }
        return jdbcClient.sql("SELECT * FROM USERS WHERE id = :id")
                .param("id", id)
                .query(User.class)
                .single();
    }

    public void delete(UUID id) {
        jdbcClient.sql("DELETE FROM USERS WHERE id = :id")
                .param("id", id)
                .update();
    }
}

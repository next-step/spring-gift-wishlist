package gift.repository;

import gift.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcClient jdbc;
    public UserRepository(JdbcClient jdbc) {this.jdbc = jdbc;}

    public void save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();
        jdbc.sql("""
                    INSERT INTO users (email, password, name, created_at, updated_at) 
                    VALUES (:email, :password, :name, :created_at, :updated_at)
                """)
                .param("email",user.getEmail())
                .param("password", user.getPassword())
                .param("name", user.getName())
                .param("created_at", now)
                .param("updated_at", now)
                .update(keyHolder);
        Map<String, Object> keys = keyHolder.getKeys();
        Long id = (Long) Objects.requireNonNull(keys).get("id");
        user.setId(id);
    }

    public boolean existsByEmail(String email) {
        Long count = jdbc.sql("SELECT COUNT(*) FROM users WHERE email = :email")
                        .param("email", email)
                        .query(Long.class)
                        .single();
        return count > 0;
    }
    public Optional<Long> getIdFromEmail(String email) {
        return jdbc.sql("SELECT id FROM users WHERE email = :email")
                        .param("email", email)
                        .query(Long.class)
                        .optional();
    }

    public Optional<User> findByEmail(String email) {
        return jdbc.sql("SELECT * FROM users WHERE email = :email")
                .param("email", email)
                .query(User.class)
                .optional();
    }

    public boolean checkPassword(String userEmail, String requestPassword) {
        String userPwd = jdbc.sql("SELECT password FROM users WHERE email = :email")
                .param("email", userEmail)
                .query(String.class)
                .single();
        return BCrypt.checkpw(requestPassword, userPwd);
    }
}

package gift.repository;

import gift.entity.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class H2UserRepository {

    private final JdbcClient jdbcClient;

    public H2UserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<User> findByEmail(String email) {
        return jdbcClient.sql("select * from users where email = :email")
                .param("email", email)
                .query(User.class)
                .optional();
    }

    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("insert into users (email, password) values (:email, :password)")
                .param("email", user.getEmail())
                .param("password", user.getPassword())
                .update(keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;
    }
}

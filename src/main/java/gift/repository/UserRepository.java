package gift.repository;

import gift.dto.user.UserRequestDto;
import gift.dto.user.UserResponseDto;
import gift.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Repository
public class UserRepository {
    private final JdbcClient jdbc;
    public UserRepository(JdbcClient jdbc) {this.jdbc = jdbc;}

    public UserResponseDto save(UserRequestDto userRequestDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String hashPwd = makeHashPwd(userRequestDto.getPassword());
        LocalDateTime now = LocalDateTime.now();
        jdbc.sql("""
                    INSERT INTO users (email, password, name, created_at, updated_at) 
                    VALUES (:email, :password, :name, :created_at, :updated_at)
                """)
                .param("email",userRequestDto.getEmail())
                .param("password", hashPwd)
                .param("name", userRequestDto.getName())
                .param("created_at", now)
                .param("updated_at", now)
                .update(keyHolder);
        Map<String, Object> keys = keyHolder.getKeys();
        Long id = (Long) Objects.requireNonNull(keys).get("id");
        User new_user = userRequestDto.toEntity();
        new_user.setId(id);
        new_user.setPassword(hashPwd);
        return new UserResponseDto(makeToken(new_user));
    }

    public UserResponseDto access(UserRequestDto userRequestDto) {
        User access_user = userRequestDto.toEntity();
        access_user.setId(GetIdFromEmail(userRequestDto.getEmail()));
        return new UserResponseDto(makeToken(access_user));
    }

    public boolean existsByEmail(String email) {
        Long count = jdbc.sql("SELECT COUNT(*) FROM users WHERE email = :email")
                        .param("email", email)
                        .query(Long.class)
                        .single();
        return count > 0;
    }
    public Long GetIdFromEmail(String email) {
        Long get_id = jdbc.sql("SELECT id FROM users WHERE email = :email")
                        .param("email", email)
                .query(Long.class)
                .single();
        return get_id;
    }
    public boolean checkPassword(String userEmail, String requestPassword) {
        String userPwd = jdbc.sql("SELECT password FROM users WHERE email = :email")
                .param("email", userEmail)
                .query(String.class)
                .single();
        String checkPwd = makeHashPwd(requestPassword);
        return BCrypt.checkpw(requestPassword, userPwd);
    }
    private String makeToken(User user) {
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    private String makeHashPwd(String password) {
        return BCrypt.hashpw(
                password,
                BCrypt.gensalt());
    }
}

package gift.auth;

import gift.user.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtProvider {
    private final SecretKey secretKey;

    public JwtProvider(@Value("${jwt.secret}") String keyString) {
        this.secretKey = Keys.hmacShaKeyFor(keyString.getBytes());
    }
    public String createToken(User user) {
        return Jwts.builder()
                .claim("sub", user.getEmail())
                .signWith(secretKey)
                .compact();
    }
}

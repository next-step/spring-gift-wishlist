package gift.auth;

import gift.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtProvider {
    private final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    public String createToken(User user) {
        return Jwts.builder()
                .claim("sub", user.getEmail())
                .signWith(secretKey)
                .compact();
    }
}

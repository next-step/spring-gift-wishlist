package gift.user;

import gift.user.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

  public String generateToken(User user) {
    return Jwts.builder()
        .setSubject(user.getId().toString())
        .claim("email", user.getEmail())
        .claim("role", user.getRole().toString())
        .expiration(createExpirationDate())
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
        .compact();
  }

  private Date createExpirationDate() {
    long EXPIRATION_MINUTES = 30;
    return new Date(System.currentTimeMillis() + EXPIRATION_MINUTES * 60 * 1000);
  }
}

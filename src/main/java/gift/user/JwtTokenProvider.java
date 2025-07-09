package gift.user;

import gift.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
  private final SecretKey secretKeySpec;
  private final JwtParser jwtParser;

  public JwtTokenProvider() {

    this.secretKeySpec = Keys.hmacShaKeyFor(secretKey.getBytes());

    this.jwtParser = Jwts.parser()
        .verifyWith(secretKeySpec)
        .build();
  }

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

  public boolean validateToken(String token) throws Exception {
    try {
      jwtParser.parse(token);
      return true;
    } catch (Exception e) {
      throw new Exception("JWT 검증 실패하였습니다.", e);
    }
  }

  public String getRole(String token) throws Exception {
    try {
      Claims claims = jwtParser.parseSignedClaims(token).getPayload();
      return claims.get("role", String.class);
    } catch (Exception e) {
      throw new Exception("토큰에서 role을 가져올 수 없습니다.", e);
    }
  }
}

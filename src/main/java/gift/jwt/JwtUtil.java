package gift.jwt;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private static final String SECRET = "KAKAOTECHCAMPUS_0707_LONG_SECRET_KEY_2025";
  private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
  private static final long EXPIRATION_TIME = 3600; // 1시간 후 만료

  // ✅ JWT 생성
  public String createToken(String email) {
    Instant now = Instant.now();

    return Jwts.builder()
        .subject(String.valueOf(email))
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(EXPIRATION_TIME)))
        .signWith(KEY)
        .compact();
  }
  // ✅ JWT 유효성 검사
  public boolean isValidToken(String token) {
    try {
      JwtParser parser = Jwts.parser()
          .verifyWith((SecretKey) KEY)
          .build();

      parser.parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  // ✅ JWT 이메일 추출
  public String getEmailFromToken(String token) {
    JwtParser parser = Jwts.parser()
        .verifyWith((SecretKey) KEY)
        .build();
    return parser.parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }
}

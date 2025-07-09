package gift.security;

import gift.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
  private static final long EXPIRATION_TIME_MS = 1000 * 60 * 60;

  private final SecretKey key;

  public JwtProvider() {
    this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));
  }

  public String generateToken(Member member) {
    return Jwts.builder()
        .subject(String.valueOf(member.getId()))
        .claim("role", member.getRole())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
        .signWith(key)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public Long getMemberId(String token) {
    return Long.valueOf(getClaims(token).getSubject());
  }

  public String getRole(String token) {
    return getClaims(token).get("role", String.class);
  }
}

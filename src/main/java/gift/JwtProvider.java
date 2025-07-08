package gift;

import gift.entity.Member;
import gift.entity.Token;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

  public Token generateToken(Member member) {
    String accessToken = Jwts.builder()
        .setSubject(
            member.getId().toString())
        .claim("email", member.getEmail())
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
        .compact();
    return new Token(accessToken);
  }
}

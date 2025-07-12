package gift.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtProvider {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String createToken(Long memberId) {
        return Jwts.builder()
                .setSubject(memberId.toString())
                .signWith(key)
                .compact();
    }

    public Long extractMemberId(String token) {
        return Long.parseLong(
                Jwts.parser()
                        .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }
}

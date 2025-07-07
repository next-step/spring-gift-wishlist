package gift.util;

import gift.domain.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String secret = "D23kjibddFAWerws2AFlL8WXmohJMCvigQggaEypa5E=";

    private final Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("email", member.getEmail())
                .claim("role", member.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getMemberIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claims.getSubject());
    }
}

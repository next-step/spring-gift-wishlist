package gift.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static Key key = null;
    private static long expireMillis = 0;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expireMillisecond}") long expireMillisecond
    ) {
        key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        expireMillis = expireMillisecond;
    }

    public static String generateToken(Long memberId, String role) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + expireMillis);
        return Jwts.builder()
                .subject(memberId.toString())
                .claim("role", role)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public static LocalDateTime getExpiration(String token) {
        Claims claims = parseToken(token).getPayload();
        return LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
    }

    public static Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseSignedClaims(token);
    }
}

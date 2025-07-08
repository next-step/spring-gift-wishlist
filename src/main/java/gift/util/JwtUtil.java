// src/main/java/gift/util/JwtUtil.java
package gift.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String generateToken(Long memberId, String role, long expireMillis) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(memberId.toString())
                .claim("role", role)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expireMillis))
                .signWith(KEY)
                .compact();
    }

    public static Jws<Claims> validate(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }
}

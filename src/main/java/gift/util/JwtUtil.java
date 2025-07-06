package gift.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = System.getenv().getOrDefault(
            "JWT_SECRET", "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E="
    );
    private static final long EXPIRATION = Long.parseLong(
            System.getenv().getOrDefault("JWT_EXPIRATION", "3600000")
    );

    private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public static String generateToken(Long memberId, String email) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim("email", email)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY)
                .compact();
    }
}

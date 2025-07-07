package gift.auth;

import gift.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    private final SecretKey secretKey;
    private final long timeout_ms;

    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.token_timeout_s}") long timeOut_s) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.timeout_ms = timeOut_s * 1000;
    }

    public String generateToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + timeout_ms);

        return Jwts.builder()
                .setSubject(member.getEmail())
                .claim("role", member.getRole().name())
                .setIssuedAt(now)
                .signWith(secretKey)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

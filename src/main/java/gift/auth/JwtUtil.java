package gift.auth;

import gift.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long timeoutMs;

    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.tokenTimeoutSec}") long timeOut_s) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.timeoutMs = timeOut_s * 1000;
    }

    public String generateToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + timeoutMs);

        return Jwts.builder()
                .setSubject(member.getEmail())
                .claim("role", member.getRole().name())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey)
                .compact();
    }

    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        }
        catch (Exception e){
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

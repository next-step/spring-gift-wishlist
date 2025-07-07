package gift.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final long accessTokenExpirationDayToMills;
    private final SecretKey secretKey;

    public JwtService(JwtProperty jwtProperty) {
        this.accessTokenExpirationDayToMills =
                TimeUnit.MILLISECONDS.convert(jwtProperty.accessTokenExpirationDay(), TimeUnit.DAYS);
        this.secretKey = Keys.hmacShaKeyFor(jwtProperty.secretKey().getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Long memberId, String email, String role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpirationDayToMills);

        return Jwts.builder()
                .setSubject(memberId.toString())
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractTokenFromHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new RuntimeException("유효하지 않은 헤더");
    }

    // todo: 인증 구현 더 해야함
}

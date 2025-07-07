package gift.util;

import com.fasterxml.jackson.databind.ser.Serializers.Base;
import gift.entity.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String BEARER_PREFIX = "Bearer ";

    private static final long TOKEN_TIME = 30 * 60 * 1000L;

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(String email) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + TOKEN_TIME);

        return Jwts.builder()
            .setSubject(email)
            .claim("email", email)
            .setIssuedAt(now)
            .setExpiration(expiredDate)
            .signWith(key)
            .compact();
    }
}
package gift.config;

import gift.entity.MemberRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;

    public String generateToken(Long id, MemberRole role) {

        return Jwts.builder()
                   .subject(id.toString())
                   .claim("role", role.name())
                   .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                   .compact();
    }

}

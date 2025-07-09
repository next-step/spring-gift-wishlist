package gift.security;

import gift.entity.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private int expiration;

    public String generateJwtToken(Member member) {
        return Jwts.builder()
                .subject(member.getEmail())
                .claim("role", member.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}

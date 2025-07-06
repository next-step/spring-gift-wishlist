package gift.auth;

import gift.entity.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
    
    @Value(value = "${jwt.secret}")
    private String secretKey;
    
    public String createToken(Member member) {
        return Jwts.builder()
            .subject(Long.toString(member.getId()))
            .claim("email", member.getEmail())
            .claim("role", member.getRole())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }
}

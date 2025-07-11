package gift.config;

import gift.entity.MemberRole;
import gift.exception.UnAuthenticationException;
import io.jsonwebtoken.Claims;
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

    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                       .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                       .build()
                       .parseSignedClaims(token)
                       .getPayload();
        } catch (RuntimeException e) {
            throw new UnAuthenticationException("로그인 정보가 유효하지 않습니다.");
        }
    }

    public Long getMemberIdFromToken(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }
}

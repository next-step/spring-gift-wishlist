package gift.domain.member.jwt;

import gift.domain.member.Member;
import gift.global.exception.BadRequestException;
import gift.global.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtProvider {
    private final String secretKey;

    public JwtProvider(@Value("${JWT_SECRET_KEY}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(Member member){
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public String extractEmailFromAccessToken(String accessToken) throws TokenExpiredException {
        Claims claims = parseClaims(accessToken);
        if (!"access".equals(claims.get("tokenType", String.class))) {
            throw new BadRequestException("사용된 토큰이 엑세스 토큰이 아닙니다. 요청하신 로직에서는 엑세스 토큰으로만 처리가 가능합니다.");
        }
        if (claims.getExpiration().before(new Date())) {
            throw new TokenExpiredException("액세스 토큰이 만료되었습니다. 리프레시 토큰으로 다시 액세스 토큰을 발급받으세요.");
        }
        return claims.getSubject();
    }

    public Claims parseClaims(String token) throws TokenExpiredException {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new TokenExpiredException(e.getMessage());
        }
    }

}

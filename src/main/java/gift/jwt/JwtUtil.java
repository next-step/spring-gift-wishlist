package gift.jwt;

import gift.common.exceptions.JwtValidationException;
import gift.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtUtil {

    private static final String AUTHORIZATION_KEY = "auth";
    private static final Long ACCESS_TOKEN_EXPIRE = 1000 * 60 * 20L; // 20분

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Member member) {
        return Jwts.builder()
            .claim("id", member.getId())
            .claim(AUTHORIZATION_KEY, member.getUserRole().name())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE))
            .signWith(secretKey)
            .compact();
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        } catch (MalformedJwtException e) {
            throw new JwtValidationException("유효하지 않은 토큰입니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtValidationException("만료된 토큰입니다.");
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            throw new JwtValidationException("지원하지 않는 형식의 토큰입니다.");
        } catch (Exception e) {
            throw new JwtValidationException("예상치 못한 오류가 발생했습니다.");
        }
    }

    private Claims parseClaims(String token) {
        token = resolveToken(token);
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (ExpiredJwtException e) {
            throw new JwtValidationException("만료된 토큰입니다.");
        }
    }

    public Long getIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.get("id", Long.class);
    }

    private String resolveToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer")) {
            return token.substring(7);
        }

        return null;
    }
}

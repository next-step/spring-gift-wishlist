package gift.util;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtProvider implements TokenProvider {

    private final SecretKey secretKey;

    public JwtProvider(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(
            secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    public String createToken(Long userId) {
        return Jwts.builder()
            .claim("userId", userId)
            .signWith(secretKey)
            .compact();
    }

    public Long getUserId(String token) {
        return Long.parseLong(
            Jwts.parser()
                .verifyWith(secretKey) // 검증
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", String.class)
        );
    }
}

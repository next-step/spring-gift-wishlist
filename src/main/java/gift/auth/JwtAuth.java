package gift.auth;

import gift.entity.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@PropertySource("classpath:secure.properties")
public class JwtAuth {

    @Value("${jwt.key}")
    private String jwtKey;

    public String createJwtToken(Member member){
        String accessToken = Jwts.builder()
                .setSubject(member.getEmail())
                .claim("email", member.getEmail())
                .signWith(getSecretKeyFromJWTKey(jwtKey))
                .compact();
        return accessToken;
    }

    public String getEmailFromToken(String token) {
        SecretKey key = getSecretKeyFromJWTKey(jwtKey);
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("email", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSecretKeyFromJWTKey(jwtKey))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT Token: " + e);
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT Token: " + e);
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty. " + e);
        } catch (Exception e) {
            System.out.println("JWT Signature is invalid. " + e);
        }
        return false;
    }

    private SecretKey getSecretKeyFromJWTKey(String jwtKey) {
        return Keys.hmacShaKeyFor(jwtKey.getBytes());
    }
}

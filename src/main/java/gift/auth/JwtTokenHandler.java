package gift.auth;

import gift.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenHandler {

    private final String secretKey;
    private final String tokenType = "Bearer ";

    public JwtTokenHandler(@Value("${app.jwt.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String createToken(User user) {
        return Jwts.builder()
            .header()
            .add("typ", "JWT")
            .and()
            .claim("userRole", user.userRole())
            .claim("email", user.email())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) //30분
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }

    public boolean verifyToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload();
        return claims.get("email", String.class);
    }

    public String getUserRoleFromToken(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload();
        return claims.get("userRole", String.class);
    }

    public String getEmailFromHeader(String header) {
        String token = header.substring(tokenType.length());
        Claims claims = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload();
        return claims.get("email", String.class);
    }
}

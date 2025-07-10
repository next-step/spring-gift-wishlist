package gift.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key key;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(Long memberId, String name, String email, String role) {
        return Jwts.builder()
                .subject(memberId.toString())
                .claim("name", name)
                .claim("email", email)
                .claim("role", role)
                .signWith(key)
                .compact();
    }

    public Long getMemberId(String token) {
        return Long.valueOf(Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }
}

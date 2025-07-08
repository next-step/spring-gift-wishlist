package gift.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private final Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

    public String createToken(Long memberId, String name, String email, String role) {
        return Jwts.builder()
                .subject(memberId.toString())
                .claim("name", name)
                .claim("email", email)
                .claim("role", role)
                .signWith(key)
                .compact();
    }
}

package gift.member.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public JwtProvider() {

    }

    public String generateToken(Long memberId, String email, String role) {
        // access token
        return Jwts.builder()
            .setSubject(memberId.toString())
            .claim("email", email)
            .claim("role", role)
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }
}

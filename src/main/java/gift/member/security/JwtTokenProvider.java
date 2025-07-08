package gift.member.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

    public JwtTokenProvider() {

    }

    public String generateToken(Long memberId, String email, String role) {
        // access token
        return Jwts.builder()
            .setSubject(memberId.toString())
            .claim("email", email)
            .claim("role", role)
            .signWith(key)
            .compact();
    }
}

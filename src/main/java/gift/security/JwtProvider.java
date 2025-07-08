package gift.security;

import gift.entity.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private static final byte[] KEY_BYTES = SECRET_KEY.getBytes(StandardCharsets.UTF_8);

    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId() != null ? member.getId().toString() : member.getEmail())
                .claim("email", member.getEmail())
                .signWith(Keys.hmacShaKeyFor(KEY_BYTES))
                .compact();
    }


}


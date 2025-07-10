package gift.service;

import gift.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    private String key = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E";

    @Override
    public Optional<Member> isValidateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(key.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            Member find = new Member(claims.get("id", Long.class), claims.get("email", String.class), null,
                    claims.get("role", String.class));
            return Optional.of(find);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public String createAccessToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("id", member.getId())
                .claim("email", member.getEmail())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(key.getBytes()))
                .compact();
    }
}

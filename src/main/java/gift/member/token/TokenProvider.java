package gift.member.token;

import gift.member.dto.MemberResponseDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class TokenProvider {
    private final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String generateToken(MemberResponseDto memberResponseDto) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(memberResponseDto.id().toString())
                .claim("member", memberResponseDto.email())
                .claim("role", memberResponseDto.role())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 1000 * 60 * 60))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}

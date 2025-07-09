package gift.util;

import gift.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final String secretKey;


    public JwtTokenProvider(@Value("${jwt.secretKey}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String makeJwtToken(Member member) {
        return Jwts.builder()
                .subject(Long.toString(member.id()))
                .claim("email", member.email())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public JwtPayload parseJwtToken(String jwtToken) {
        JwtParser parser = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build();
        Claims claims = (Claims) parser.parse(jwtToken).getPayload();

        return new JwtPayload(claims);
    }
}


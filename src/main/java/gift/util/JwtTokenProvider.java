package gift.util;

import gift.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;


    public JwtTokenProvider(@Value("${jwt.secretKey}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());

    }

    public String makeJwtToken(Member member) {
        return Jwts.builder()
                .subject(Long.toString(member.id()))
                .claim("email", member.email())
                .signWith(secretKey)
                .compact();
    }

    public JwtPayload parseJwtToken(String jwtToken) {
        JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)
                .build();
        Claims claims = (Claims) parser.parse(jwtToken).getPayload();

        return new JwtPayload(claims);
    }
}


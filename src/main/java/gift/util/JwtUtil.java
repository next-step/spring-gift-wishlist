package gift.util;

import gift.dto.MemberRequestDto;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    @Value("${jwt-secret-key}")
    private String secret;

    private JwtParser jwtParser;

    private final long oneHour = 60 * 60 * 1000;

    @PostConstruct
    public void init() {
        jwtParser = Jwts.parser().setSigningKey(secret).build();
    }

    public String extractEmail(String token){
        return jwtParser
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public String generateToken(MemberRequestDto memberRequestDto) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(memberRequestDto.getEmail())
                .claim("role", memberRequestDto.getRole())
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + oneHour))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token); // 서명 검증
            return !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }


    private Boolean isTokenExpired(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}

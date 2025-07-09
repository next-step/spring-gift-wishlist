package gift.member.util;

import gift.member.entity.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String rawSecret;

    private SecretKey key;

    @PostConstruct
    void init() {
        // 문자열 → SecretKey 로 변환
        this.key = Keys.hmacShaKeyFor(rawSecret.getBytes());
    }

    public String createToken(Member member) {
        return Jwts.builder()
            .subject(member.getId().toString())
            .claim("email", member.getEmail())
            .signWith(key)
            .compact();
    }
}

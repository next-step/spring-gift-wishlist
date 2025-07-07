package gift.util;

import gift.entity.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtProvider {
    private final String RAW_SECRET = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private SecretKey key;

    @PostConstruct
    void init() {
        // 문자열 → SecretKey 로 변환
        this.key = Keys.hmacShaKeyFor(RAW_SECRET.getBytes());
    }

    public String createToken(Member member) {
        return Jwts.builder()
            .subject(member.getId().toString())
            .claim("role", member.getEmail())
            .signWith(key)
            .compact();
    }
}

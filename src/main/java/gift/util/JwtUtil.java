package gift.util;

import gift.entity.member.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtUtil {

    private final SecretKey key;
    private final long expirationMillis;

    /**
     * @param base64Secret     Base64 인코딩된 256비트 비밀키 문자열
     * @param expirationMillis 토큰 만료 시간(밀리초 단위), 예: 3600_000L (1시간)
     */
    public JwtUtil(String base64Secret, long expirationMillis) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(decodedKey);
        this.expirationMillis = expirationMillis;
    }

    /**
     * 주어진 Member 정보를 클레임에 담아 액세스 토큰을 생성합니다.
     */
    public String generateAccessToken(Member member) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .subject(member.getId().id().toString())
                .claim("email", member.getEmail().email())
                .claim("role", member.getRole().name())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰을 파싱하고 유효성을 검증하여 Claims를 반환합니다.
     *
     * @throws JwtException (ExpiredJwtException, MalformedJwtException 등)
     */
    public Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }
}

package gift.common.util;

import gift.entity.UserRole;
import gift.common.model.CustomAuth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.*;
import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TokenProvider implements InitializingBean {
    private static final String AUTHORITIES_KEY = "auth";
    private static final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private final String secret;
    private final Long expiration;
    private SecretKey secretKey;


    public TokenProvider(
        @Value("${gift.jwt.secret}") @Valid String secret,
        @Value("${gift.jwt.expiration}") @Valid Long expiration
    ) {
        this.secret = secret;
        this.expiration = expiration;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (secret == null || secret.isEmpty()) {
            throw new Exception(("설정을 통해 JWT 비밀 키를 제공해야 합니다.\njwt.secret=your_secret_key"));
        }
        if (expiration == null || expiration <= 0) {
            throw new Exception("설정을 통해 JWT 만료 기간을 제공해야 합니다.\njwt.expiration=your_expiration_time_in_seconds");
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String userId, List<UserRole> authorities) {
        Instant now = Instant.now(Clock.systemDefaultZone());
        Instant expiryDate = now.plusSeconds(expiration);

        String authoritiesString = authorities.stream()
                .map(UserRole::toString)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(userId)
                .claim(AUTHORITIES_KEY, authoritiesString)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiryDate))
                .signWith(this.secretKey)
                .compact();
    }

    public CustomAuth getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String userId = claims.getSubject();
        String authoritiesString = claims.get(AUTHORITIES_KEY, String.class);
        List<UserRole> authorities = Stream.of(authoritiesString.split(","))
                .map(UserRole::fromString)
                .collect(Collectors.toList());
        if (userId == null || authorities.isEmpty()) {
            return null;
        }
        return new CustomAuth(userId, authorities);
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(this.secretKey)
                .build()
                .parse(token);
            return true;
        } catch (SecurityException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (MalformedJwtException e) {
            log.info("잘못된 JWT 토큰입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (Exception e) {
            log.info("유효하지 않은 JWT 토큰입니다.");
        }
        return false;
    }
}

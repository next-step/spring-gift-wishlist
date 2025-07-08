package gift.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 토큰 생성 메서드:{@link #createAccessToken(Long, String)}
 * 토큰 검증 메서드: {@link #validateToken(String)}
 * 토큰 정보 추출 메서드: {@link #getEmailFromToken(String)}, {@link #getMemberIdFromToken(String)}
 * 토큰 만료/시간 관련 메서드: {@link #getRemainingTimeInSeconds(String)}
 * 토큰 검증 결과 객체: {@link JwtTokenProvider.TokenValidationResult}
 */
@Component
public class JwtTokenProvider implements JwtTokenPort {

    private final String issuer;
    private final String audience;
    private final long accessTokenExpirationMinutes;
    private final long refreshTokenExpirationDays;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtTokenProvider(
            @Value("${jwt.secret:Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=}") String secretKey,
            @Value("${jwt.issuer:spring-gift-wishlist}") String issuer,
            @Value("${jwt.audience:spring-gift-api}") String audience,
            @Value("${jwt.access-token-expiration-minutes:15}") long accessTokenExpirationMinutes,
            @Value("${jwt.refresh-token-expiration-days:7}") long refreshTokenExpirationDays) {
        this.issuer = issuer;
        this.audience = audience;
        this.accessTokenExpirationMinutes = accessTokenExpirationMinutes;
        this.refreshTokenExpirationDays = refreshTokenExpirationDays;
        Algorithm algo = Algorithm.HMAC256(secretKey);
        this.algorithm = algo;
        this.verifier = JWT.require(algo)
                .withIssuer(issuer)
                .withAudience(audience)
                .build();
    }

    @Override
    public String createAccessToken(Long memberId, String email) {
        Instant now = Instant.now();
        Instant expiration = now.plus(accessTokenExpirationMinutes, ChronoUnit.MINUTES);

        return JWT.create()
                .withSubject(email)
                .withClaim("memberId", memberId)
                .withClaim("email", email)
                .withClaim("tokenType", "access")
                .withIssuer(issuer)
                .withAudience(audience)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiration))
                .sign(algorithm);
    }

    @Override
    public String createRefreshToken(Long memberId, String email) {
        Instant now = Instant.now();
        Instant expiration = now.plus(refreshTokenExpirationDays, ChronoUnit.DAYS);

        return JWT.create()
                .withSubject(email)
                .withClaim("memberId", memberId)
                .withClaim("email", email)
                .withClaim("tokenType", "refresh")
                .withIssuer(issuer)
                .withAudience(audience)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiration))
                .sign(algorithm);
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public TokenValidationResult validateToken(String token) {
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            return TokenValidationResult.valid(decodedJWT);
        } catch (TokenExpiredException e) {
            return TokenValidationResult.expired();
        } catch (JWTVerificationException e) {
            return TokenValidationResult.invalid(e.getMessage());
        }
    }

    @Override
    public String getEmailFromToken(String token) {
        TokenValidationResult result = validateToken(token);
        if (!result.isValid()) {
            throw new IllegalArgumentException("Invalid token: " + result.getErrorMessage());
        }
        return result.getDecodedJWT().getClaim("email").asString();
    }

    @Override
    public Long getMemberIdFromToken(String token) {
        TokenValidationResult result = validateToken(token);
        if (!result.isValid()) {
            throw new IllegalArgumentException("Invalid token: " + result.getErrorMessage());
        }
        return result.getDecodedJWT().getClaim("memberId").asLong();
    }

    @Override
    public String getTokenTypeFromToken(String token) {
        TokenValidationResult result = validateToken(token);
        if (!result.isValid()) {
            throw new IllegalArgumentException("Invalid token: " + result.getErrorMessage());
        }
        return result.getDecodedJWT().getClaim("tokenType").asString();
    }

    @Override
    public long getRemainingTimeInSeconds(String token) {
        TokenValidationResult result = validateToken(token);
        if (!result.isValid()) {
            return 0;
        }
        Date expiration = result.getDecodedJWT().getExpiresAt();
        return (expiration.getTime() - System.currentTimeMillis()) / 1000;
    }

    public static class TokenValidationResult {
        private final boolean valid;
        private final boolean expired;
        private final String errorMessage;
        private final DecodedJWT decodedJWT;

        private TokenValidationResult(boolean valid, boolean expired, String errorMessage, DecodedJWT decodedJWT) {
            this.valid = valid;
            this.expired = expired;
            this.errorMessage = errorMessage;
            this.decodedJWT = decodedJWT;
        }

        public static TokenValidationResult valid(DecodedJWT decodedJWT) {
            return new TokenValidationResult(true, false, null, decodedJWT);
        }

        public static TokenValidationResult expired() {
            return new TokenValidationResult(false, true, "Token has expired", null);
        }

        public static TokenValidationResult invalid(String errorMessage) {
            return new TokenValidationResult(false, false, errorMessage, null);
        }

        public boolean isValid() { return valid; }
        public boolean isExpired() { return expired; }
        public String getErrorMessage() { return errorMessage; }
        public DecodedJWT getDecodedJWT() { return decodedJWT; }
    }
} 
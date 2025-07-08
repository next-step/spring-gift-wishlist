package gift.token.service;

import gift.exception.EntityNotFoundException;
import gift.exception.InvalidCredentialsException;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import gift.token.entity.RefreshToken;
import gift.token.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final Duration ACCESS_TOKEN_TTL = Duration.ofMinutes(15);

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenProvider(MemberRepository memberRepository,
            RefreshTokenRepository refreshTokenRepository) {
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String generateAccessToken(Member member) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_TTL.toMillis());
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(member.getUuid().toString())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public String generateAccessToken(String refreshTokenString) throws IllegalArgumentException {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenString)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));
        if (refreshToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new InvalidCredentialsException("Refresh token has expired");
        }

        Member member = memberRepository.findByUuid(refreshToken.getMemberUuid())
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        return generateAccessToken(member);
    }

    @Transactional
    public String generateRefreshToken(Member member) {
        RefreshToken refreshToken = new RefreshToken(member.getUuid());
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateAccessToken(String token) {
        Date expirationDate = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return !expirationDate.before(new Date());
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

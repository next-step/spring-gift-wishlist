package gift.common.jwt;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtTokenPort {
    String createAccessToken(Long memberId, String email);
    String createRefreshToken(Long memberId, String email);
    JwtTokenProvider.TokenValidationResult validateToken(String token);
    String getEmailFromToken(String token);
    Long getMemberIdFromToken(String token);
    String getTokenTypeFromToken(String token);
    long getRemainingTimeInSeconds(String token);
    String resolveToken(HttpServletRequest request);
}

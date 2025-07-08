package gift.member.application.port.in.dto;

import java.time.Instant;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        Instant accessTokenExpiry,
        Instant refreshTokenExpiry
) {

    public static AuthResponse withRefreshToken(
            String accessToken, 
            String refreshToken, 
            long expiresInSeconds,
            Instant accessTokenExpiry,
            Instant refreshTokenExpiry) {
        return new AuthResponse(
                accessToken, 
                refreshToken, 
                "Bearer", 
                expiresInSeconds,
                accessTokenExpiry,
                refreshTokenExpiry
        );
    }
} 
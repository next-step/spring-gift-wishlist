package gift.auth.dto;

import gift.auth.domain.TokenResponse;

public record LoginResponseDto(
    String tokenType,
    String accessToken,
    long expiresIn,
    String refreshToken,
    long refreshTokenExpiresIn
) {

  public static LoginResponseDto from(TokenResponse res) {
    return new LoginResponseDto(res.tokenType(), res.accessToken(), res.expiresIn(),
        res.refreshToken(), res.refreshTokenExpiresIn());
  }
}

package gift.auth.service;

import gift.auth.domain.JwtUtils;
import gift.auth.domain.TokenResponse;
import gift.auth.repository.MemberAuthRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TokenService {

  private final JwtUtils jwtUtils;
  private final MemberAuthRepository memberAuthRepository;
  private static final String TOKEN_TYPE = "bearer";

  public TokenService(JwtUtils jwtUtils, MemberAuthRepository memberAuthRepository) {
    this.jwtUtils = jwtUtils;
    this.memberAuthRepository = memberAuthRepository;
  }

  public TokenResponse generateBearerTokenResponse(Long memberId, String email) {
    String accessToken = jwtUtils.createToken(memberId, email, List.of());
    String refreshToken = jwtUtils.createRefreshToken(memberId);

    memberAuthRepository.updateRefreshToken(memberId, refreshToken);

    long accessTokenExpiresIn = jwtUtils.getAccessTokenExpirationTime();
    long refreshTokenExpiresIn = jwtUtils.getRefreshTokenExpirationTime();

    return new TokenResponse(TOKEN_TYPE, accessToken, accessTokenExpiresIn, refreshToken,
        refreshTokenExpiresIn);
  }

  public boolean isValidToken(String token) {
    return jwtUtils.validateToken(token);
  }

  public String getEmail(String token) {
    return jwtUtils.getEmail(token);
  }

  public Long getUserId(String token) {
    return jwtUtils.getUserId(token);
  }
}

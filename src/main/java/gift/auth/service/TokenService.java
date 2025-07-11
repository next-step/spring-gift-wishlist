package gift.auth.service;

import gift.auth.domain.JwtProvider;
import gift.auth.domain.TokenResponse;
import gift.auth.repository.MemberAuthRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TokenService {

  private final JwtProvider jwtProvider;
  private final MemberAuthRepository memberAuthRepository;
  private static final String TOKEN_TYPE = "bearer";

  public TokenService(JwtProvider jwtProvider, MemberAuthRepository memberAuthRepository) {
    this.jwtProvider = jwtProvider;
    this.memberAuthRepository = memberAuthRepository;
  }

  public TokenResponse generateBearerTokenResponse(Long memberId, String email) {
    String accessToken = jwtProvider.createToken(memberId, email, List.of());
    String refreshToken = jwtProvider.createRefreshToken(memberId);

    memberAuthRepository.updateRefreshToken(memberId, refreshToken);

    long accessTokenExpiresIn = jwtProvider.getAccessTokenExpirationTime();
    long refreshTokenExpiresIn = jwtProvider.getRefreshTokenExpirationTime();

    return new TokenResponse(TOKEN_TYPE, accessToken, accessTokenExpiresIn, refreshToken,
        refreshTokenExpiresIn);
  }

  public boolean isValidToken(String token) {
    return jwtProvider.validateToken(token);
  }

  public String getEmail(String token) {
    return jwtProvider.getEmail(token);
  }

  public Long getUserId(String token) {
    return jwtProvider.getUserId(token);
  }
}

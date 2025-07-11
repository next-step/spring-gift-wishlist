package com.example.demo.service.refreshtoken;

import com.example.demo.repository.refreshtoken.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  public void saveRefreshToken(Long id, String refreshToken) {
    refreshTokenRepository.saveRefreshToken(id, refreshToken);
  }
}

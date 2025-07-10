package com.example.demo.service;

import com.example.demo.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{

  private final RefreshTokenRepository refreshTokenRepository;

  public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  public void saveRefreshToken(Long id, String refreshToken) {
    refreshTokenRepository.saveRefreshToken(id, refreshToken);
  }
}

package com.example.demo.service.refreshtoken;

public interface RefreshTokenService {

  void saveRefreshToken(Long id, String refreshToken);
}

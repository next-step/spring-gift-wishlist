package com.example.demo.repository.refreshtoken;

public interface RefreshTokenRepository {

  void saveRefreshToken(Long id, String refreshToken);
  boolean existsByUserId(Long id);
  String refreshTokenFindByUserId(Long id);
  void refreshTokenDeleteByUserId(Long id);
}

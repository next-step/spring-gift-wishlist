package com.example.demo.repository;

import org.springframework.stereotype.Repository;

public interface RefreshTokenRepository {
  void saveRefreshToken(String email, String refreshToken);
  String refreshTokenFindByEmail(String email);
  void refreshTokenDeleteByEmail(String email);
}

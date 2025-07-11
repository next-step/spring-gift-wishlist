package com.example.demo.repository;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

  private final JdbcClient jdbcClient;

  public RefreshTokenRepositoryImpl(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }
  @Override
  public boolean existsByUserId(Long id) {
    String sql = "SELECT COUNT(*) FROM refresh_token WHERE user_id = :userId";
    Integer count = jdbcClient.sql(sql)
                              .param("userId", id)
                              .query(Integer.class)
                              .single();
    return count != null && count > 0;
  }

  @Override
  public void saveRefreshToken(Long id, String refreshToken) {
    if (existsByUserId(id)) {
      updateRefreshToken(id, refreshToken);
    } else {
      insertRefreshToken(id, refreshToken);
    }
  }

  private void updateRefreshToken(Long id, String refreshToken) {
    String sql = "UPDATE refresh_token SET refresh_token = :refresh_token WHERE user_id = :userId";
    jdbcClient.sql(sql)
              .param("refresh_token", refreshToken)
              .param("userId", id)
              .update();
  }

  private void insertRefreshToken(Long id, String refreshToken) {
    String sql = "INSERT INTO refresh_token(user_id, refresh_token) VALUES (:userId, :refresh_token)";
    jdbcClient.sql(sql)
              .param("userId", id)
              .param("refresh_token", refreshToken)
              .update();
  }

    @Override
  public String refreshTokenFindByUserId(Long id) {
    String sql = "SELECT refresh_token FROM refresh_token WHERE user_id = :userId";
    return jdbcClient.sql(sql)
                     .param("userId", id)
                     .query(String.class)
                     .optional()
                     .orElse(null);
  }

  @Override
  public void refreshTokenDeleteByUserId(Long id) {
    String sql = "DELETE FROM refresh_token WHERE user_id = :userId";
    jdbcClient.sql(sql)
              .param("userId", id)
              .update();
  }
}

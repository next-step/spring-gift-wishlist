package com.example.demo.repository;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository{

  private final JdbcClient jdbcClient;

  public RefreshTokenRepositoryImpl(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public void saveRefreshToken(String email, String refreshToken) {
    String selectSql = "SELECT COUNT(*) FROM refresh_token WHERE email = :email";
    Integer count = jdbcClient.sql(selectSql)
                              .param("email", email)
                              .query(Integer.class)
                              .single();

    if (count != null && count > 0) {
      String updateSql = "UPDATE refresh_token SET refresh_token = :refresh_token WHERE email = :email";
      jdbcClient.sql(updateSql)
                .param("refresh_token", refreshToken)
                .param("email", email)
                .update();
    } else {
      String insertSql = "INSERT INTO refresh_token(email, refresh_token) VALUES (:email, :refresh_token);";
      jdbcClient.sql(insertSql)
                .param("refresh_token", refreshToken)
                .param("email", email)
                .update();
    }
  }

  @Override
  public String refreshTokenFindByEmail(String email) {
    String sql = "SELECT refresh_token FROM refresh_token WHERE email = :email";
    return jdbcClient.sql(sql)
        .param("email", email)
        .query(String.class)
        .optional()
        .orElse(null);
  }

  @Override
  public void refreshTokenDeleteByEmail(String email) {
    String sql = "DELETE FROM refresh_token WHERE email = :email";
    jdbcClient.sql(sql)
        .param("email", email)
        .update();
  }
}

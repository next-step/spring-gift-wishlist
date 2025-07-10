package com.example.demo.repository;

import com.example.demo.entity.User;
import java.util.Optional;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

  private final JdbcClient jdbcClient;

  public UserRepositoryImpl(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  @Override
  public Optional<User> findByEmail(String email) {
    String sql = "SELECT id, email, password, role FROM users WHERE email = :email";
    return jdbcClient.sql(sql)
        .param("email", email)
        .query(User.class)
        .optional();
  }

  @Override
  public void saveUser(User user) {
    String sql = "INSERT INTO users (email, password, role) VALUES (:email, :password:, :role)";

    jdbcClient.sql(sql)
        .param("email", user.getEmail())
        .param("password", user.getPassword())
        .param("role", user.getRole())
        .update();
  }

  @Override
  public void deleteByEmail(String email) {
    String sql = "DELETE FROM users WHERE email = :email";

    jdbcClient.sql(sql)
        .param("email", email)
        .update();
  }
}

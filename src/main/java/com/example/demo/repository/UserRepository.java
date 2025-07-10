package com.example.demo.repository;

import com.example.demo.entity.User;
import java.util.Optional;

public interface UserJdbcClientRepository {
  Optional<User> findByEmail(String email);
  void saveUser(User user);
  void deleteByEmail(String email);
}

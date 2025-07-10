package com.example.demo.entity;

import com.example.demo.security.PasswordHasher;

public class User {
  private Long id;
  private String email;
  private String password; // 해시된 상태로만 유지됨
  private String role;

  public User() {}

  public User(Long id, String email, String hashedPassword, String role) {
    this.id = id;
    this.email = email;
    this.password = hashedPassword;
    this.role = role;
  }

  public static User createWithRawPassword(Long id, String email, String rawPassword, String role) {
    return new User(id, email, PasswordHasher.hash(rawPassword), role);
  }

  public void changePassword(String rawPassword) {
    this.password = PasswordHasher.hash(rawPassword);
  }


  public boolean isPasswordMatch(String rawInput) {
    return this.password.equals(PasswordHasher.hash(rawInput));
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getRole() {
    return role;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setRole(String role) {
    this.role = role;
  }
}

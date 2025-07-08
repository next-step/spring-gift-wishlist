package com.example.demo.entity;

public class User {
  private Long id;
  private String email;
  private String password;
  private String role;

  public User() {}

  public User(Long id, String email, String password, String role){
    this.id = id;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public Long getId(){
    return id;
  }
  public String getEmail(){
    return email;
  }
  public String getPassword(){
    return password;
  }
  public String getRole(){
    return role;
  }
  public void setId(Long id){
    this.id = id;
  }
  public void setEmail(String email){
    this.email = email;
  }
  public void setPassword(String password){
    this.password = password;
  }
  public void setRole(String role){
    this.role = role;
  }
}

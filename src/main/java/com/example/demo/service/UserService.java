package com.example.demo.service;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.entity.User;

public interface UserService {

  User login(UserRequestDto dto);
  User findByEmail(String email);
  void deleteByEmail(String email);
}

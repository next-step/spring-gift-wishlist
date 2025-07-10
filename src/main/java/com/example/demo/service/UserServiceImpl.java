package com.example.demo.service;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.entity.User;
import com.example.demo.exception.InvalidLoginException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.security.PasswordHasher;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User login(UserRequestDto dto) {
    User user = userRepository.findByEmail(dto.getEmail())
                              .orElseThrow(() -> new InvalidLoginException("이메일이 일치하지 않습니다."));

    String hashedInput = PasswordHasher.hash(dto.getPassword());
    if(!user.isPasswordMatch(dto.getPassword())){
      throw new InvalidLoginException("비밀번호가 일치하지 않습니다.");
    }
    return user;
  }

  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
                         .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
  }

  @Override
  public void deleteByEmail(String email) {
    if(userRepository.findByEmail(email).isEmpty()) {
      throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
    }
    userRepository.deleteByEmail(email);
  }
}

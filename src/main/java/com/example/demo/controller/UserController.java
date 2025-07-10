package com.example.demo.controller;

import com.example.demo.dto.UserDataInfo;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.entity.User;
import com.example.demo.jwt.Jwt;
import com.example.demo.jwt.JwtProvider;
import com.example.demo.service.RefreshTokenService;
import com.example.demo.service.UserService;
import com.example.demo.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final UserService userService;
  private final RefreshTokenService refreshTokenService;
  private final JwtProvider jwtProvider;

  public UserController(UserService userService, RefreshTokenService refreshTokenService,
      JwtProvider jwtProvider) {
    this.userService = userService;
    this.refreshTokenService = refreshTokenService;
    this.jwtProvider = jwtProvider;
  }


  @PostMapping("/login")
  public ResponseEntity<Jwt> login(@RequestBody @Valid UserRequestDto dto){
    User user = userService.login(dto);

    if(user == null){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Jwt jwt = jwtProvider.createJwt(user.getEmail(), user.getRole());
    refreshTokenService.saveRefreshToken(user.getId(), jwt.getRefreshToken());

    return ResponseEntity.ok()
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getAccessToken())
        .body(jwt);
  }

  @GetMapping("/users/me")
  public ResponseEntity<UserDataInfo> me(HttpServletRequest request) {
    String email = (String) request.getAttribute("userEmail");
    User user = userService.findByEmail(email);
    return ResponseEntity.ok(new UserDataInfo(user));
  }

}

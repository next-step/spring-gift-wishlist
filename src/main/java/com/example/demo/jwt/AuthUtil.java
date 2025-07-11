package com.example.demo.jwt;

import com.example.demo.entity.User;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.user.UserService;
import org.springframework.http.HttpHeaders;

import jakarta.servlet.http.HttpServletRequest;

public class AuthUtil {

  public static User extractUserFromRequest(HttpServletRequest request, JwtProvider jwtProvider, UserService userService) {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new UnauthorizedException("AccessToken이 없습니다.");
    }

    try {
      String token = authHeader.replace("Bearer ", "").trim();
      String email = jwtProvider.getClaims(token).get("email", String.class);
      return userService.findByEmail(email);
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new UnauthorizedException("AccessToken이 유효하지 않습니다.");
    }
  }
}

package com.example.demo.jwt;

import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

  private final JwtProvider jwtProvider;
  private final UserService userService;

  public JwtAuthInterceptor(JwtProvider jwtProvider, UserService userService) {
    this.jwtProvider = jwtProvider;
    this.userService = userService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return false;
    }
    try{
      String token = authHeader.replace("Bearer ", "").trim();
      String email = jwtProvider.getClaims(token).get("email", String.class);

      request.setAttribute("userEmail", email);
      return true;
    } catch (Exception e){
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      return false;
    }
  }
}
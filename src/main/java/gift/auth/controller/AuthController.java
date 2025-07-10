package gift.auth.controller;

import gift.auth.domain.CustomUserDetails;
import gift.auth.dto.MemberRegisterRequestDto;
import gift.auth.dto.LoginRequestDto;
import gift.auth.dto.RefreshTokenRequestDto;
import gift.auth.dto.TokenResponseDto;
import gift.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<TokenResponseDto> registerMember(@RequestBody MemberRegisterRequestDto dto){
    TokenResponseDto responseDto = authService.registerMember(dto);
    return ResponseEntity.ok(responseDto);
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto dto){
    TokenResponseDto responseDto = authService.login(dto);
    return ResponseEntity.ok(responseDto);
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokenResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto dto){
    TokenResponseDto responseDto = authService.refreshToken(dto);
    return ResponseEntity.ok(responseDto);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(Authentication authentication){
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    authService.logout(userDetails.getUsername());
    return ResponseEntity.noContent().build();
  }
}

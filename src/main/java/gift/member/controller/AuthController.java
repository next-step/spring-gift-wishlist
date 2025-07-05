package gift.member.controller;

import gift.member.dto.LoginRequestDto;
import gift.member.dto.TokenResponseDto;
import gift.member.exception.LoginFailedException;
import gift.member.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members/")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(
        @Valid @RequestBody LoginRequestDto loginRequestDto) {

        return new ResponseEntity<>(authService.login(loginRequestDto), HttpStatus.OK);
    }

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<String> handleLoginFailedException(LoginFailedException ex) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body("오류: " + ex.getMessage());
    }

}

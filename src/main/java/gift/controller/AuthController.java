package gift.controller;

import gift.dto.AuthLogInRequestDto;
import gift.dto.AuthResponseDto;
import gift.dto.AuthSignUpRequestDto;
import gift.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> signUp(@Valid @RequestBody AuthSignUpRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerMember(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthLogInRequestDto requestDto) {
        return  ResponseEntity.ok(authService.findMemberToLogIn(requestDto));
    }
}

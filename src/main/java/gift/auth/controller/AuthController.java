package gift.auth.controller;

import gift.user.domain.User;
import gift.auth.dto.UserLoginRequestDto;
import gift.auth.service.AuthService;
import gift.auth.dto.UserSingupRequestDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> singup(@RequestBody UserSingupRequestDto userSignupRequestDto) {
        User user = authService.signUp(userSignupRequestDto);

        return ResponseEntity
                .created(URI.create("/api/auth/" + user.getId()))
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        try {
            String token = authService.login(userLoginRequestDto);
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer"+token).body(token);
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}

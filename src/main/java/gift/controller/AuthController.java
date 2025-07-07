package gift.controller;

import gift.dto.request.LoginRequest;
import gift.dto.request.SignUpRequest;
import gift.dto.response.TokenResponse;
import gift.exception.user.InValidPasswordException;
import gift.exception.user.NoUserException;
import gift.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static gift.status.UserErrorStatus.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<TokenResponse> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(signUpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(authService.login(loginRequest));
    }

    // * Error Handling
    @ExceptionHandler(InValidPasswordException.class)
    public ResponseEntity<String> handleException(InValidPasswordException e) {
        return ResponseEntity.status(INVALID_PASSWORD.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(NoUserException.class)
    public ResponseEntity<String> handleException(NoUserException e) {
        return ResponseEntity.status(NO_USER.getStatus()).body(e.getMessage());
    }
}

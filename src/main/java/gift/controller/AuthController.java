package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.SigninRequest;
import gift.dto.SigninResponse;
import gift.dto.SignupRequest;
import gift.dto.SignupResponse;
import gift.service.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(
        @Valid @RequestBody SignupRequest request
    ){
        SignupResponse response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> signin(
        @Valid @RequestBody SigninRequest request
    ) {
        SigninResponse response = authService.signin(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.CreateUserRequest;
import gift.dto.CreateUserResponse;
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
    public ResponseEntity<CreateUserResponse> signup(
        @Valid @RequestBody CreateUserRequest request
    ){
        CreateUserResponse response = authService.createUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

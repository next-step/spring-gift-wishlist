package gift.controller;

import gift.dto.MemberLoginRequest;
import gift.dto.MemberRegisterRequest;
import gift.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final AuthService authService;

    public MemberController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody MemberRegisterRequest request) {
        String token = authService.registerAndIssueToken(request);
        return ResponseEntity.status(201).body(new TokenResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody MemberLoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new TokenResponse(token));
    }


    private record TokenResponse(String token) {}
}
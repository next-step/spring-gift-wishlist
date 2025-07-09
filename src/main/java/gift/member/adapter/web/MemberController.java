package gift.member.adapter.web;

import gift.member.application.port.in.LoginMemberUseCase;
import gift.member.application.port.in.RegisterMemberUseCase;
import gift.member.application.port.in.dto.AuthResponse;
import gift.member.application.port.in.dto.LoginRequest;
import gift.member.application.port.in.dto.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final RegisterMemberUseCase registerMemberUseCase;
    private final LoginMemberUseCase loginMemberUseCase;

    public MemberController(RegisterMemberUseCase registerMemberUseCase, LoginMemberUseCase loginMemberUseCase) {
        this.registerMemberUseCase = registerMemberUseCase;
        this.loginMemberUseCase = loginMemberUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = registerMemberUseCase.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = loginMemberUseCase.login(request);
        return ResponseEntity.ok(response);
    }
} 
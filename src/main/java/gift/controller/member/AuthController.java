// src/main/java/gift/controller/AuthController.java
package gift.controller.member;

import gift.dto.member.AuthResponse;
import gift.dto.member.LoginRequest;
import gift.dto.member.RegisterRequest;
import gift.service.member.MemberService;
import gift.util.BasicAuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class AuthController {

    private final MemberService memberService;

    public AuthController(MemberService svc) {
        this.memberService = svc;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        AuthResponse resp = memberService.register(req);
        return ResponseEntity.status(201).body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        AuthResponse resp = memberService.login(req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login/basic")
    public ResponseEntity<AuthResponse> loginWithBasic(
            @RequestHeader("Authorization") String authHeader) {
        BasicAuthUtil.Credentials cred = BasicAuthUtil.parse(authHeader);
        String email = cred.email();
        String password = cred.password();
        
        AuthResponse resp = memberService.loginByBasic(email, password);
        return ResponseEntity.ok(resp);
    }
}

package gift.controller;

import gift.dto.MemberRequest;
import gift.dto.TokenResponse;
import gift.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody MemberRequest request) {
        TokenResponse response = memberService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody MemberRequest request) {
        TokenResponse response = memberService.login(request);
        return ResponseEntity.ok().body(response);
    }
}

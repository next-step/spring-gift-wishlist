package gift.controller;

import gift.dto.AuthorizationRequest;
import gift.dto.AuthorizationResponse;
import gift.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthorizationResponse> register(@RequestBody AuthorizationRequest request){
        AuthorizationResponse response = memberService.register(request);

        return ResponseEntity.created(null)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthorizationResponse> login(@RequestBody AuthorizationRequest request){
        AuthorizationResponse response = memberService.login(request);

        return ResponseEntity.ok()
                .body(response);
    }

}

package gift.Controller;

import gift.Entity.Member;
import gift.dto.MemberRequest;
import gift.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginRestController {

    private final MemberService memberService;

    public LoginRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequest memberRequest) {
        try {
            String token = memberService.login(memberRequest.getId(), memberRequest.getPassword());
            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


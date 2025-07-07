package gift.Controller;

import gift.service.MemberService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/api/members")
public class MemberController {
  private final MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @PostMapping("/register")
  public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> req) {
    String token = memberService.register(req.get("email"), req.get("password"));
    return ResponseEntity.ok().body(Map.of("token", token));
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> req) {
    String token = memberService.login(req.get("email"), req.get("password"));
    return ResponseEntity.ok().body(Map.of("token", token));
  }
}

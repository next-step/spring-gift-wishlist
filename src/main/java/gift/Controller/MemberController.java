package gift.Controller;

import gift.jwt.JwtUtil;
import gift.service.MemberService;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {
  private final MemberService memberService;
  private final JwtUtil jwtUtil;

  public MemberController(MemberService memberService,  JwtUtil jwtUtil) {
    this.memberService = memberService;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/register")
  public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> req) {
    memberService.register(req.get("email"), req.get("password"));
    return ResponseEntity.ok().body(Map.of("message", "정상 회원가입 되었습니다"));
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> req) {
    String token = memberService.login(req.get("email"), req.get("password"));
    return ResponseEntity.ok().body(Map.of("token", token));
  }

  @GetMapping("/me")
  public ResponseEntity<?> getMyInfo(@RequestHeader("Authorization") String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization 헤더가 유효하지 않습니다.");
    }

    String token = authHeader.substring(7); // "Bearer " 제거

    if (!jwtUtil.isValidToken(token)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
    }

    String email = jwtUtil.getEmailFromToken(token);

    return memberService.findByEmail(email)
        .<ResponseEntity<?>>map(member -> ResponseEntity.ok(Map.of("id", member.getId())))
        .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("회원 정보를 찾을 수 없습니다."));
  }
}

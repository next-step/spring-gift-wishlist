package gift.Controller;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.model.Member;
import gift.jwt.JwtUtil;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  public ResponseEntity<MemberResponseDto> register(@RequestBody MemberRequestDto req) {
    memberService.register(req.getEmail(), req.getPassword());
    MemberResponseDto response = new MemberResponseDto();
    response.setEmail(req.getEmail());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody MemberRequestDto req) {
    String token = memberService.login(req.getEmail(), req.getPassword());
    return ResponseEntity.ok(Map.of("token", token));
  }

  @GetMapping("/me")
  public ResponseEntity<MemberResponseDto> getMyInfo(HttpServletRequest request) {
    Member member = (Member) request.getAttribute("member");
    if(member == null) {
      return ResponseEntity.status(401).build();
    }
    return ResponseEntity.ok(MemberResponseDto.from(member));
  }
}

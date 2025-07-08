package gift.controller;

import gift.dto.MemberLoginRequestDto;
import gift.dto.MemberLoginResponseDto;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

  private final MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @PostMapping("/register")
  public ResponseEntity<MemberLoginResponseDto> register(@RequestBody @Valid MemberLoginRequestDto memberLoginRequestDto) {
    MemberLoginResponseDto memberLoginResponseDto = memberService.register(memberLoginRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(memberLoginResponseDto);
  }

  @PostMapping("/login")
  public ResponseEntity<MemberLoginResponseDto> login(@RequestBody @Valid MemberLoginRequestDto memberLoginRequestDto) {
    MemberLoginResponseDto memberLoginResponseDto = memberService.login(memberLoginRequestDto);
    return ResponseEntity.ok(memberLoginResponseDto);
  }
}

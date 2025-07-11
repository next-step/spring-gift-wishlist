package gift.controller;

import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.service.MemberService;
import gift.util.JwtUtil;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Member;
import java.sql.SQLOutput;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> registerMember(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        memberService.saveMember(memberRequestDto);
        String token = jwtUtil.generateToken(memberRequestDto);
        return ResponseEntity.ok(new TokenResponseDto(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@Valid @RequestBody MemberRequestDto memberRequestDto) {

      if(!memberService.existMember(memberRequestDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        String token = jwtUtil.generateToken(memberRequestDto);
        return ResponseEntity.ok(new TokenResponseDto(token));
    }
}

package gift.controller;

import gift.dto.LoginRequestDto;
import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.dto.TokenResponseDto;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원 가입
    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register(@Valid @RequestBody MemberRequestDto requestDto) {
        TokenResponseDto response = memberService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        TokenResponseDto response = memberService.login(requestDto);
        return ResponseEntity.ok(response);
    }

    // 회원 조회 (ID로)
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long id) {
        MemberResponseDto response = memberService.getMember(id);
        return ResponseEntity.ok(response);
    }

    // 회원 조회 (이메일로)
    @GetMapping("/email/{email}")
    public ResponseEntity<MemberResponseDto> getMemberByEmail(@PathVariable String email) {
        MemberResponseDto response = memberService.getMemberByEmail(email);
        return ResponseEntity.ok(response);
    }

    // 전체 회원 조회
    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getAllMembers() {
        List<MemberResponseDto> response = memberService.getAllMembers();
        return ResponseEntity.ok(response);
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
} 
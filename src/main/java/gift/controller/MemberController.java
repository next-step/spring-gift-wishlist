package gift.controller;

import gift.dto.LoginRequestDto;
import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
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
} 
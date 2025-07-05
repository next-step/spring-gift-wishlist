package gift.controller;

import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.service.MemberService;
import gift.util.JwtUtil;
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
    public ResponseEntity<?> registerMember(@RequestBody MemberRequestDto memberRequestDto) {
        memberService.saveMember(memberRequestDto);
        String token = jwtUtil.generateToken(memberRequestDto);
        System.out.println(token);

        return ResponseEntity.ok(new TokenResponseDto(token));
    }
}

package gift.controller;

import gift.dto.MemberLogInRequestDto;
import gift.dto.MemberLogInResponseDto;
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
    public ResponseEntity<MemberLogInResponseDto> signUp(@Valid @RequestBody MemberLogInRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.registerMember(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLogInResponseDto> login(@Valid @RequestBody MemberLogInRequestDto requestDto) {
        return  ResponseEntity.ok(memberService.findMemberToLogIn(requestDto));
    }
}

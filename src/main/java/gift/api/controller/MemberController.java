package gift.api.controller;

import gift.api.dto.MemberRequestDto;
import gift.api.dto.TokenResponseDto;
import gift.api.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> registerMember(
            @Valid @RequestBody MemberRequestDto memberRequestDto
    ) {
        TokenResponseDto response = memberService.registerMember(memberRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginMember(
            @Valid @RequestBody MemberRequestDto memberRequestDto
    ) {
        TokenResponseDto response = memberService.loginMember(memberRequestDto);

        return ResponseEntity.ok(response);
    }
}

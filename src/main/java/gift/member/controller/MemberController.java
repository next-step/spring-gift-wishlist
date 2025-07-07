package gift.member.controller;

import gift.member.dto.MemberInfo;
import gift.member.dto.MemberLoginRequestDto;
import gift.member.dto.MemberLoginResponseDto;
import gift.member.dto.MemberRegisterRequestDto;
import gift.member.service.MemberService;
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
    public ResponseEntity<MemberInfo> register(
            @Valid @RequestBody MemberRegisterRequestDto requestDto) {
        var createdMember = memberService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMember);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponseDto> login(
            @Valid @RequestBody MemberLoginRequestDto requestDto) {
        var loginResponse = memberService.login(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

}

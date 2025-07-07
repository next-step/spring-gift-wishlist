package gift.member.controller;

import gift.common.security.JwtTokenProvider;
import gift.member.dto.LoginResponseDto;
import gift.member.dto.MemberCreateDto;
import gift.member.dto.MemberResponseDto;
import gift.member.service.MemberService;
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
    private final JwtTokenProvider jwtTokenProvider;

    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> registerMember(
        @RequestBody MemberCreateDto memberCreateDto
    ) {
        MemberResponseDto createdMember = memberService.create(memberCreateDto);

        LoginResponseDto loginResponseDto = new LoginResponseDto(
            jwtTokenProvider.generateToken(
                createdMember.id(),
                createdMember.name(),
                createdMember.email()
            )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponseDto);
    }

}

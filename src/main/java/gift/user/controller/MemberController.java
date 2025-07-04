package gift.user.controller;

import gift.user.dto.LoginRequestDto;
import gift.user.dto.LoginResponseDto;
import gift.user.dto.RegisterRequestDto;
import gift.user.dto.RegisterResponseDto;
import gift.user.service.MemberService;
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
    public ResponseEntity<RegisterResponseDto> registerMember(
        @Valid @RequestBody RegisterRequestDto registerRequestDto) {

        return new ResponseEntity<>(memberService.registerMember(registerRequestDto),
            HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginMember(
        @Valid @RequestBody LoginRequestDto loginRequestDto) {

        return new ResponseEntity<>(memberService.loginMember(loginRequestDto), HttpStatus.OK);
    }
}

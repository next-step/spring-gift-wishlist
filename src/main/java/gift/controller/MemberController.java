package gift.controller;

import gift.dto.AuthResponseDto;
import gift.dto.MemberRequestDto;
import gift.service.MemberService;
import org.apache.coyote.BadRequestException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerMember(
            @Validated @RequestBody MemberRequestDto memberRequestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                memberService.creatMember(memberRequestDto)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Validated @RequestBody MemberRequestDto memberRequestDto
    ) throws AuthenticationException {
        return ResponseEntity.ok(
                memberService.login(memberRequestDto)
        );
    }
}

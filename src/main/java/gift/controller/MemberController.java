package gift.controller;

import gift.dto.AuthResponseDto;
import gift.dto.MemberRequestDto;
import gift.service.MemberService;
import org.apache.coyote.BadRequestException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;
    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerMember(
            @Validated @RequestBody MemberRequestDto memberRequestDto
    ) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                memberService.creatMember(memberRequestDto)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Validated @RequestBody MemberRequestDto memberRequestDto
    ) throws AuthenticationException {
        return ResponseEntity.ok(
                memberService.authenticateMember(memberRequestDto)
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        log.trace(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}

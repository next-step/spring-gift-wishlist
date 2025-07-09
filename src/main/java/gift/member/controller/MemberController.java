package gift.member.controller;

import gift.member.dto.RegisterRequestDto;
import gift.member.dto.TokenResponseDto;
import gift.member.exception.MemberNotFoundException;
import gift.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    // TODO: 같은 이메일을 입력할 경우, 예외 처리 해야겠네.
    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> registerMember(
        @Valid @RequestBody RegisterRequestDto registerRequestDto) {

        return new ResponseEntity<>(memberService.registerMember(registerRequestDto),
            HttpStatus.CREATED);

    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(MemberNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body("오류: " + ex.getMessage());
    }
}

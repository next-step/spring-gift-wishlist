package gift.controller;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.service.AuthMemberService;
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

    private AuthMemberService authMemberService;

    public MemberController(AuthMemberService authMemberService) {
        this.authMemberService = authMemberService;
    }

    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> signUp(@RequestBody @Valid MemberRequestDto requestDto) {
        MemberResponseDto responseDto = authMemberService.signUp(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto> login(@RequestBody @Valid MemberRequestDto requestDto) {
        MemberResponseDto responseDto = authMemberService.login(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}

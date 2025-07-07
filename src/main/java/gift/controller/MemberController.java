package gift.controller;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<MemberResponseDto> create(@RequestBody MemberRequestDto requestDto) {
        return new ResponseEntity<>(memberService.create(requestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto> login(
        @RequestBody MemberRequestDto requestDto,
        @RequestHeader("Authorization") String bearerAuthorizationHeader
    ) {
        return new ResponseEntity<>(memberService.login(requestDto, bearerAuthorizationHeader),
            HttpStatus.OK);
    }
}

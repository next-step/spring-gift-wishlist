package gift.controller;

import gift.service.MemberService;
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

    public final MemberService memberService;

    public MemberController(MemberService memberService) {

        this.memberService = memberService;
    }
}
@PostMapping("/register")
public ResponseEntity<MemberResponseDto> registerMember(
        @RequestBody @Valid MemberRequestDto requestDto) {

    String token = memberService.register(requestDto); // 토큰 생성
    MemberResponseDto responseDto = new MemberResponseDto(token);
    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);

}
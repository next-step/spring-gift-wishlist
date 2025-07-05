package gift.controller.member;

import gift.dto.api.member.MemberRequestDto;
import gift.dto.api.member.MemberResponseDto;
import gift.service.member.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    
    MemberService memberService;
    
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    
    //회원가입
    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> registerMember(
        @RequestBody MemberRequestDto requestDto
    ) {
        MemberResponseDto responseDto = memberService.registerMember(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
    
    //로그인
    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto> loginMember(
        @RequestBody MemberRequestDto requestDto
    ) {
        MemberResponseDto responseDto = memberService.loginMember(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}

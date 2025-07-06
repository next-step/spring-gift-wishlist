package gift.controller;

import gift.dto.CreateMemberRequestDto;
import gift.dto.JWTResponseDto;
import gift.service.MemberService;
import gift.service.ProductService;
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

    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<JWTResponseDto> createMember(
            @Valid @RequestBody CreateMemberRequestDto requestDto){
        return new ResponseEntity<>(memberService.createMember(requestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JWTResponseDto> loginMember(
            @Valid @RequestBody CreateMemberRequestDto requestDto){
        return new ResponseEntity<>(memberService.loginMember(requestDto), HttpStatus.OK);
    }
}

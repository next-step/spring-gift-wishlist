package gift.controller;

import gift.dto.api.MemberRegisterRequestDto;
import gift.dto.api.MemberRegisterResponseDto;
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

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원 생성
    @PostMapping
    public ResponseEntity<MemberRegisterResponseDto> createMember(
        @RequestBody @Valid MemberRegisterRequestDto requestDto
    ) {
        String token = memberService.registerMember(requestDto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new MemberRegisterResponseDto(token));
    }

}

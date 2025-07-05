package gift.controller;

import gift.dto.api.MemberRegisterRequestDto;
import gift.entity.Member;
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
    public ResponseEntity<Member> createMember(@RequestBody @Valid MemberRegisterRequestDto dto) {
        Member member = new Member(dto.getEmail(), dto.getPassword());
        Member saved = memberService.registerMember(member);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

}

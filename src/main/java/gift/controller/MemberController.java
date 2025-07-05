package gift.controller;

import gift.dto.MemberRequestDto;
import gift.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Member;
import java.sql.SQLOutput;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerMember(@RequestBody MemberRequestDto memberRequestDto) {
        memberService.saveMember(memberRequestDto);
        return ResponseEntity.ok().build();
    }
}

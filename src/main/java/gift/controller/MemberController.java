package gift.controller;

import gift.dto.*;
import gift.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register(@RequestBody MemberRegisterRequestDto memberRegisterRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.register(memberRegisterRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        return ResponseEntity.ok(memberService.login(memberLoginRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> findAllMembers() {
        return ResponseEntity.ok(memberService.findAllMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> findMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.findMemberById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMember(@PathVariable Long id, @RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        memberService.updateMember(id, memberUpdateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

}

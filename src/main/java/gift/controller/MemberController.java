package gift.controller;

import gift.dto.*;
import gift.entity.Member;
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
        TokenResponseDto tokenDto = memberService.register(memberRegisterRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenDto);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        String token = memberService.login(memberLoginRequestDto);
        return ResponseEntity.ok(new TokenResponseDto(token));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> list() {
        List<MemberResponseDto> members = memberService.findAllMembers().stream()
                .map(member -> new MemberResponseDto(
                        member.getId(),
                        member.getName(),
                        member.getEmail(),
                        member.getRole().name()))
                .toList();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> get(@PathVariable Long id) {
        Member member = memberService.findMemberById(id);
        MemberResponseDto dto = new MemberResponseDto(member.getId(), member.getName(), member.getEmail(), member.getRole().name());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        memberService.updateMember(id, memberUpdateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

}

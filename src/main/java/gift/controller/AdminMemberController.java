package gift.controller;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/members")
public class AdminMemberController {

    private final MemberService memberService;

    public AdminMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 전체 회원 조회
    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getAllMembers() {
        List<MemberResponseDto> response = memberService.getAllMembers();
        return ResponseEntity.ok(response);
    }

    // 회원 조회 (ID로)
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long id) {
        MemberResponseDto response = memberService.getMember(id);
        return ResponseEntity.ok(response);
    }

    // 회원 추가 (관리자가 직접 추가)
    @PostMapping
    public ResponseEntity<MemberResponseDto> createMember(@RequestBody MemberRequestDto requestDto) {
        // 추후 관리자 인증 로직 추가할 것 같음 ...
        memberService.register(requestDto);
        MemberResponseDto response = memberService.getMemberByEmail(requestDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 회원 수정
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponseDto> updateMember(@PathVariable Long id, 
                                                       @RequestBody MemberRequestDto requestDto) {
        MemberResponseDto response = memberService.updateMember(id, requestDto);
        return ResponseEntity.ok(response);
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
} 
package gift.controller.member;

import gift.domain.Member;
import gift.dto.jwt.TokenResponse;
import gift.dto.member.LoginRequest;
import gift.dto.member.MemberRequest;
import gift.dto.member.MemberResponse;
import gift.service.member.MemberService;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestBody LoginRequest loginRequest
    ){
        TokenResponse token = memberService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK)
            .body(token);
    }

    // member 생성: 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> createMember(
        @RequestBody MemberRequest memberRequest
    ){
        Long memberId = memberService.insert(memberRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(memberId);
    }

    // member 조회: 관리자용 기능
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMember(
        @PathVariable Long memberId
    ){
        MemberResponse member = memberService.findById(memberId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(member);
    }


    // member 수정: 관리자용 기능
    @PatchMapping("/{memberId}")
    public ResponseEntity<?> updateMember(
        @RequestBody MemberRequest memberRequest,
        @PathVariable Long memberId
    ){
        memberService.update(memberId, memberRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    // member 삭제: 관리자용 기능
    @PostMapping("/delete/{memberId}")
    public ResponseEntity<?> deleteMember(
        @PathVariable Long memberId
    ){
        memberService.deleteById(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

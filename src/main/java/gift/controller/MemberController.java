package gift.controller;

import gift.dto.CreateMemberRequest;
import gift.dto.LoginMemberRequest;
import gift.dto.CreateMemberResponse;
import gift.dto.LoginMemberResponse;
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

    @PostMapping
    public ResponseEntity<CreateMemberResponse> createMember (
            @Valid @RequestBody CreateMemberRequest createMemberRequest
    ) {
        Member member = memberService.createMember(createMemberRequest.email(), createMemberRequest.password());
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CreateMemberResponse.from(member));
    }

}

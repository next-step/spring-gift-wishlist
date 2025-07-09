package gift.controller;

import gift.dto.CreateMemberRequest;
import gift.dto.LoginMemberRequest;
import gift.dto.CreateMemberResponse;
import gift.dto.LoginMemberResponse;
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

    @PostMapping("/register")
    public ResponseEntity<CreateMemberResponse> createMember (
            @Valid @RequestBody CreateMemberRequest createMemberRequest
    ) {
        memberService.createMember(createMemberRequest.email(), createMemberRequest.password());

        String token = memberService.login(
            createMemberRequest.email(),
            createMemberRequest.password()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreateMemberResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginMemberResponse> login (
            @Valid @RequestBody LoginMemberRequest loginMemberRequest
    ) {
        String token = memberService.login(
            loginMemberRequest.email(),
            loginMemberRequest.password()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new LoginMemberResponse(token));
    }
}

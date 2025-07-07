package gift.controller;

import gift.dto.CreateMemberRequest;
import gift.dto.LoginMemberRequest;
import gift.dto.CreateMemberResponse;
import gift.dto.LoginMemberResponse;
import gift.service.MemberService;
import gift.token.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberController(MemberService memberService,
                            AuthenticationManager authenticationManager,
                            JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<CreateMemberResponse> createMember (
            @Valid @RequestBody CreateMemberRequest createMemberRequest
    ) {
        memberService.createMember(createMemberRequest.email(), createMemberRequest.password());

        String token = authenticateAndGenerateToken(
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
        String token = authenticateAndGenerateToken(
            loginMemberRequest.email(),
            loginMemberRequest.password()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new LoginMemberResponse(token));
    }

    private String authenticateAndGenerateToken(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        return jwtTokenProvider.createToken(authentication.getName());
    }

}

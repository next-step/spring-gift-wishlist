package gift.controller;

import gift.config.JwtUtil;
import gift.dto.MemberRequest;
import gift.dto.MemberResponse;
import gift.dto.TokenResponse;
import gift.service.MemberService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody MemberRequest request) {
        TokenResponse response = memberService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody MemberRequest request) {
        TokenResponse response = memberService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<TokenResponse> updateMember(@RequestBody MemberRequest memberRequest) {
        if (!jwtUtil.validateToken(memberRequest.token())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        TokenResponse response = memberService.updateMember(memberRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteMember(
        @PathVariable String email,
        @RequestBody MemberRequest request
    ) {
        if (!jwtUtil.validateToken(request.token())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        memberService.deleteMember(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{email}")
    public ResponseEntity<MemberResponse> getMember(
        @PathVariable String email,
        @RequestBody MemberRequest request
    ) {
        if (!jwtUtil.validateToken(request.token())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        MemberResponse response = memberService.getMember(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers(@RequestBody MemberRequest request) {
        if (!jwtUtil.validateToken(request.token())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<MemberResponse> responses = memberService.getAllMembers();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

}

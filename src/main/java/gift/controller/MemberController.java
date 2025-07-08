package gift.controller;

import gift.common.dto.request.MemberRequestDto;
import gift.common.dto.response.TokenDto;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenDto> register(@RequestBody @Valid MemberRequestDto request) {
        TokenDto response = memberService.handleRegisterRequest(request);
        return ResponseEntity.created(URI.create("")).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid MemberRequestDto request) {
        TokenDto response = memberService.handleLoginRequest(request);
        return ResponseEntity.ok(response);
    }
}

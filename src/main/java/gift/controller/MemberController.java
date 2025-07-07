package gift.controller;

import gift.common.dto.request.MemberRequestDto;
import gift.common.dto.response.TokenDto;
import gift.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenDto> register(@RequestHeader Map<String, String> header,
                                             @RequestBody MemberRequestDto request) {
        if (!header.containsKey("host") || !header.get("host").equals("localhost:8080")) {
            throw new SecurityException("Invalid Host!");
        }
        TokenDto response = memberService.handleRegisterRequest(request);
        return ResponseEntity.created(URI.create("")).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestHeader Map<String, String> header,
                                          @RequestBody MemberRequestDto request) {
        if (!header.containsKey("host") || !header.get("host").equals("localhost:8080")) {
            throw new SecurityException("Invalid Host!");
        }
        TokenDto response = memberService.handleLoginRequest(request);
        return ResponseEntity.ok(response);
    }
}

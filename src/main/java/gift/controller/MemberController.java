package gift.controller;

import gift.common.dto.request.CreateMemberDto;
import gift.common.dto.response.TokenResponseDto;
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
    public ResponseEntity<TokenResponseDto> register(@RequestHeader Map<String, String> header,
                                                     @RequestBody CreateMemberDto request) {
        if (!header.containsKey("host") || !header.get("host").equals("localhost:8080")) {
            throw new SecurityException("Invalid Host!");
        }
        TokenResponseDto response = memberService.handleRegisterRequest(request);
        return ResponseEntity.created(URI.create("")).body(response);
    }
}

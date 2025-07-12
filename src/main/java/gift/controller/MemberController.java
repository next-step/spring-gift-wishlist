package gift.controller;

import gift.domain.Member;
import gift.dto.LoginRequest;
import gift.dto.LoginResponse;
import gift.dto.MemberResponse;
import gift.dto.RegisterRequest;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request) {
        if (memberService.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Member saved = memberService.register(request.getEmail(), request.getPassword());
        String token = memberService.createTokenFor(saved);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new LoginResponse(token));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return memberService.authenticateAndGenerateToken(
                        request.getEmail(), request.getPassword())
                .map(token -> ResponseEntity.ok(new LoginResponse(token)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMyInfo(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return memberService.findById(memberId)
                .map(member -> ResponseEntity.ok(new MemberResponse(member.getId(), member.getEmail())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}

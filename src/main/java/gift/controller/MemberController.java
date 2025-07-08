package gift.controller;

import gift.dto.request.MemberRequest;
import gift.dto.response.MemberResponse;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> register(@RequestBody @Valid MemberRequest request) {
        memberService.register(request.email(), request.pwd());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{email}")
                .buildAndExpand(request.email())
                .toUri();

        return ResponseEntity.created(location)
                .body(Map.of("message", "회원가입이 완료되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(@RequestBody @Valid MemberRequest request){
        String token = memberService.login(request.email(), request.pwd());
        return ResponseEntity.ok(new MemberResponse(token));
    }
}

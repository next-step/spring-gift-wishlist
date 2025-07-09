package gift.controller;

import gift.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import gift.service.MemberService;

import java.util.Base64;
import java.util.StringTokenizer;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService service;
    public MemberController(MemberService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Member member) {
        service.register(member);
        return ResponseEntity.ok("회원가입 성공!");
    }

    @PostMapping("/gettoken")
    public ResponseEntity<String> login(@RequestBody Member member) {
        boolean valid = service.login(member.getEmail(), member.getPassword());

        if (valid) {
            String token = service.createToken(member.getEmail(), member.getPassword());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 또는 비밀번호가 올바르지 않습니다.");
        }
    }
}

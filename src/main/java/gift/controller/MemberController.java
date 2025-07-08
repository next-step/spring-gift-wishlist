package gift.controller;

import gift.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/login")
    public ResponseEntity<String> login(HttpServletRequest request) {
        String memberHeader = request.getHeader("Authorization");
        if (memberHeader == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 정보 없음!");
        }

        String base64Credentials = memberHeader.substring("Basic ".length());
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        StringTokenizer tokenizer = new StringTokenizer(credentials, ":");
        
        String email = tokenizer.nextToken();
        String password = tokenizer.nextToken();
        boolean valid = service.login(email, password);

        if (valid) {
            return ResponseEntity.ok("로그인 성공!");
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("로그인 실패");
        }
    }
}

package gift.controller;

import gift.dto.MemberRequestDto;
import gift.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody MemberRequestDto dto) {
        memberService.register(dto);
        return new ResponseEntity<>("회원가입 성공", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody MemberRequestDto dto) {
        String token = memberService.login(dto);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

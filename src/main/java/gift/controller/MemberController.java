package gift.controller;

import gift.dto.LoginResponseDto;
import gift.dto.MemberRequestDto;
import gift.service.MemberService;
import jakarta.validation.Valid;
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
    public ResponseEntity<LoginResponseDto> register(@Valid @RequestBody MemberRequestDto dto) {
        LoginResponseDto response = memberService.saveMember(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

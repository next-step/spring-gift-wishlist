package gift.controller;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/membership")
    public ResponseEntity<MemberResponseDto> register(@Valid @RequestBody MemberRequestDto requestDto) {
        MemberResponseDto responseDto = memberService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto> login(@Valid @RequestBody MemberRequestDto requestDto) {
        MemberResponseDto responseDto = memberService.login(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}

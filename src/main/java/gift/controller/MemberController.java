package gift.controller;

import gift.dto.MemberLoginRequestDto;
import gift.dto.MemberLoginResponseDto;
import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<MemberResponseDto> createMember(@Valid @RequestBody MemberRequestDto requestDto){
        MemberResponseDto responseDto = memberService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponseDto> login(@Valid @RequestBody MemberLoginRequestDto requestDto){
        String token = memberService.login(requestDto);
        MemberLoginResponseDto responseDto = new MemberLoginResponseDto(token);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<MemberResponseDto> getMyInfo(HttpServletRequest request){
        Long id = Long.parseLong(request.getAttribute("memberId").toString());
        MemberResponseDto responseDto = memberService.find(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PutMapping
    public ResponseEntity<MemberResponseDto> updateMyInfo(
            @Valid @RequestBody MemberRequestDto requestDto,
            HttpServletRequest request){
        Long id = Long.parseLong(request.getAttribute("memberId").toString());
        MemberResponseDto responseDto = memberService.update(id, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<MemberResponseDto> deleteMyInfo(HttpServletRequest request){
        Long id = Long.parseLong(request.getAttribute("memberId").toString());
        memberService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}

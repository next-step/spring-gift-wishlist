package gift.controller;

import gift.dto.JwtResponseDto;
import gift.dto.MemberRequestDto;
import gift.entity.Member;
import gift.service.JwtAuthService;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    private final JwtAuthService jwtAuthService;

    public MemberController(MemberService memberService, JwtAuthService jwtAuthService){
        this.memberService = memberService;
        this.jwtAuthService = jwtAuthService;
    }

    //TODO: 회원가입 기능 -> 토큰을 반환
    @PostMapping("/register")
    public ResponseEntity<JwtResponseDto> register(
            @RequestBody @Valid MemberRequestDto memberRequestDto,
            HttpServletResponse response
    ){
        Member member = memberService.register(memberRequestDto);
        String token = "Bearer " + jwtAuthService.createJwt(member.getEmail(), member.getMemberId());
        response.addHeader("Authorization", token);
        return new ResponseEntity<>(new JwtResponseDto(token), HttpStatus.CREATED);
    }

    //TODO: 로그인 기능 -> 토큰을 반환
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(
            @RequestBody @Valid MemberRequestDto memberRequestDto,
            HttpServletResponse response
    ){
        String token = response.getHeader("Authorization");
        return ResponseEntity.ok().body(new JwtResponseDto(token));
    }

}

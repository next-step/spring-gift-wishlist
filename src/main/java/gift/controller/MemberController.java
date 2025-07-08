package gift.controller;

import gift.dto.JwtResponseDto;
import gift.dto.MemberRequestDto;
import gift.entity.Member;
import gift.service.JwtAuthService;
import gift.service.MemberService;
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
    public ResponseEntity<Object> register(@RequestBody @Valid MemberRequestDto memberRequestDto){
        Member member = memberService.register(memberRequestDto);
        String token = jwtAuthService.createJwt(member.getEmail(), member.getMemberId());
        return new ResponseEntity<>(new JwtResponseDto("Bearer " + token), HttpStatus.CREATED);
    }

    //TODO: 로그인 기능 -> 토큰을 반환
    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @RequestBody @Valid MemberRequestDto memberRequestDto
    ){
        //서버에 저장된 id-pw 쌍과 일치하는지 확인
        if(!memberService.checkMember(memberRequestDto)){
            //잘못된 로그인에 대해서는 403을 반환
            return new ResponseEntity<>("아이디 또는 비밀번호가 잘못되었습니다.", HttpStatus.FORBIDDEN);
        }
        //서버에 저장된 id-pw 쌍과 일치한다면 토큰을 발급
        Member member = memberService.getMemberByEmail(memberRequestDto.email()).get();
        String token = jwtAuthService.createJwt(member.getEmail(), member.getMemberId());
        return ResponseEntity.ok().body(new JwtResponseDto("Bearer " + token));
    }

}

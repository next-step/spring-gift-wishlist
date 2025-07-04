package gift.controller;

import gift.dto.MemberRequestDto;
import gift.service.JwtAuthService;
import gift.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<String> register(@RequestBody MemberRequestDto memberRequestDto){
        if(memberService.register(memberRequestDto)){
            String token = jwtAuthService.createJwt(memberRequestDto);
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().body("이미 회원으로 등록된 이메일 입니다.");
    }

    //TODO: 로그인 기능 -> 토큰을 반환
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody  MemberRequestDto memberRequestDto,
            @RequestHeader(value = "Authorization") String accessToken
    ){
        //서버에 저장된 id-pw 쌍과 일치하는지 확인
        if(memberService.checkMember(memberRequestDto)){
            return ResponseEntity.badRequest().body("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        //토큰에 대한 유효성 검사를 수행
        //jwtAuthService.checkValidation(accessToken);
        return ResponseEntity.ok().body(accessToken);
    }

}

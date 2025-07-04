package gift.controller;

import gift.dto.MemberRequestDto;
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

    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    //TODO: 회원가입 기능 -> 토큰을 반환
    @PostMapping("/register")
    public ResponseEntity<String> register(MemberRequestDto memberRequestDto){
        String token = memberService.register(memberRequestDto);
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    //TODO: 로그인 기능 -> 토큰을 반환
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody  MemberRequestDto memberRequestDto,
            @RequestHeader(value = "Authorization") String accessToken
    ){
        //서버에 저장된 id-pw 쌍과 일치하는지 확인
        if(memberService.checkMember(memberRequestDto)){

        }
        //토큰에 대한 유효성 검사를 수행
        return ResponseEntity.ok().body(accessToken);
    }

}

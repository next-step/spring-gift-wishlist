package gift.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    //TODO: 회원가입 기능 -> 토큰을 반환
    @PostMapping("/register")
    public void register(){

    }

    //TODO: 로그인 기능 -> 토큰을 반환
    @PostMapping("/login")
    public void login(){

    }

}

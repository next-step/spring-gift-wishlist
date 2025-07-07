package gift.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class MemberFrontController {

    @GetMapping("/login")
    public String loginMember() {
        return "member/login";
    }

    @GetMapping("/register")
    public String registerMember() {
        return "member/register";
    }
}

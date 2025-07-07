package gift.front.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Authorization 쿠키를 삭제
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/members/login";
    }
}

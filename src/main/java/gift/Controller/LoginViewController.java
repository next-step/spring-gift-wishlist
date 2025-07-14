package gift.Controller;

import gift.Entity.Member;
import gift.LoginResult;
import gift.dto.MemberRequest;
import gift.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginViewController {
    private final MemberService memberService;

    public LoginViewController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("member", new Member());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberRequest memberRequest,
                        Model model,
                        HttpServletResponse response) {
        try{
            LoginResult result = memberService.login(memberRequest.getId(), memberRequest.getPassword());
            // JWT를 쿠키에 저장 (HttpOnly, Secure 적용은 환경에 따라 추가)
            Cookie cookie = new Cookie("Authorization", result.getToken());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60); // 1시간
            response.addCookie(cookie);

            return "redirect:/user/products";
        }catch (Exception e){
            model.addAttribute("member", new Member());
            model.addAttribute("loginError", e.getMessage());
            return "login";
        }
    }
}
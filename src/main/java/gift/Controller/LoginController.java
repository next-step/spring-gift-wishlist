package gift.Controller;

import gift.Entity.Member;
import gift.dto.MemberRequest;
import gift.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    private final MemberService memberService;

    public LoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("member", new Member());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberRequest memberRequest,
                        Model model) {
        try{
            String token = memberService.login(memberRequest.getId(), memberRequest.getPassword());
            model.addAttribute("token", token);
            return "loginSuccess";
        }catch (Exception e){
            model.addAttribute("member", new Member());
            model.addAttribute("loginError", e.getMessage());
            return "Login";
        }
    }
}

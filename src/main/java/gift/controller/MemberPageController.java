package gift.controller;

import gift.domain.Member;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberPageController {

    private final MemberService service;

    public MemberPageController(MemberService service) {
        this.service = service;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute Member member, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        service.register(member);
        return "redirect:/members/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null || !service.login(email, password)) {
            model.addAttribute("loginError", "이메일 또는 비밀번호가 잘못되었습니다.");
            return "login";
        }

        return "redirect:/products";
    }
}

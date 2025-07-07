package gift.Controller;

import gift.Entity.Member;
import gift.Entity.Product;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {
    private final MemberService memberService;
    public RegisterController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }

    @PostMapping
    public String createProduct(@ModelAttribute @Valid Member member,
                                BindingResult bindingResult,
                                Model model) {
        if (!bindingResult.hasErrors()) {
            return "register";
        }

        memberService.register(member);
        return "redirect:/login";
    }
}

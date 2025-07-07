package gift.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminDefaultController {

    @GetMapping
    public String adminHome(Model model) {
        model.addAttribute("title", "관리자 대시보드");
        return "admin/index";
    }

    @GetMapping("/login")
    public String adminLogin(Model model) {
        model.addAttribute("title", "관리자 로그인");
        model.addAttribute("email", "");
        return "admin/login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam String email,
            @RequestParam String password,
            Model model
    ) {
        // 간단한 로그인 검증 (실제로는 서비스 레이어에서 처리해야 함)
        if ("admin@example.com".equals(email) && "password".equals(password)) {
            return "redirect:/admin";
        } else {
            model.addAttribute("error", "이메일 또는 비밀번호가 올바르지 않습니다.");
            model.addAttribute("email", email);
            return "admin/login";
        }
    }
}
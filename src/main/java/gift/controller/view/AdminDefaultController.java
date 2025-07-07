package gift.controller.view;

import gift.dto.auth.LoginRequest;
import gift.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminDefaultController {
    private final AuthService authService;

    public AdminDefaultController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String adminHome(Model model) {
        model.addAttribute("title", "관리자 대시보드");
        return "admin/index";
    }

    @GetMapping("/login")
    public String adminLogin(
            @Valid @ModelAttribute LoginRequest loginRequest,
            Model model) {
        model.addAttribute("title", "관리자 로그인");
        model.addAttribute("email", "");
        String token = authService.login(loginRequest.email(), loginRequest.password());
        return "admin/login";
    }
}
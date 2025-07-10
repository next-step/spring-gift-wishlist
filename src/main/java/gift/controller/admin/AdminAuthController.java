package gift.controller.admin;

import gift.dto.member.AuthRequest;
import gift.dto.member.AuthResponse;
import gift.service.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    private final MemberService authService;

    public AdminAuthController(MemberService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginForm(Model model,
            @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("authRequest", new AuthRequest("", ""));
        if (error != null) {
            model.addAttribute("error", "이메일 또는 비밀번호가 올바르지 않습니다.");
        }
        return "admin/login_form";
    }

    @PostMapping("/login")
    public String loginSubmit(
            @Valid @ModelAttribute("authRequest") AuthRequest form,
            BindingResult bindingResult,
            HttpServletResponse response,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/login_form";
        }

        try {
            AuthResponse authResp = authService.login(form.email(), form.password());
            String token = authResp.token();

            Cookie cookie = new Cookie("AUTH_TOKEN", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);
            response.addCookie(cookie);

            return "redirect:/admin/dashboard";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/login_form";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("AUTH_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/admin/login";
    }
}

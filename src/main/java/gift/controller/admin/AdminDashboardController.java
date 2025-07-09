package gift.controller.admin;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    // authClaims 는 JwtCookieFilter 에서 request 속성에 담긴다고 가정
    private String extractId(Claims claims) {
        return claims.getSubject();
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest req, Model model) {
        Claims claims = (Claims) req.getAttribute("authClaims");
        if (claims != null) {
            model.addAttribute("email", extractId(claims));
        }
        return "admin/dashboard";
    }
}

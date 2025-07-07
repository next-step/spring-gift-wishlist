package gift.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import gift.dto.AdminUserResponse;
import gift.dto.SignupRequest;
import gift.dto.UpdateUserRequest;
import gift.service.AuthService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/users")
public class UserAdminController {

    private final AuthService authService;

    public UserAdminController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String adminPage(Model model) {
        List<AdminUserResponse> users = authService.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/add")
    public String addPage() {
        return "addUser";
    }

    @PostMapping
    public String addUser(
        @Valid @ModelAttribute SignupRequest request
    ) {
        authService.signup(request);
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/edit")
    public String editPage(
        @PathVariable Long id,
        Model model
    ) {
        AdminUserResponse user = authService.findById(id);
        model.addAttribute("user", user);
        return "editUser";
    }

    @PutMapping("/{id}")
    public String updateUser(
        @PathVariable Long id,
        @Valid @ModelAttribute UpdateUserRequest request
    ) {
        authService.update(id, request);
        return "redirect:/admin/users";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(
        @PathVariable Long id
    ) {
        authService.delete(id);
        return "redirect:/admin/users";
    }
}

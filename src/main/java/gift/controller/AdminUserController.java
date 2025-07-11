package gift.controller;

import gift.model.User;
import gift.repository.UserDao;
import gift.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/add")
    public String addForm(Model model){
        return "user/addForm";
    }
    @PostMapping("/add")
    public String addUser(@ModelAttribute User user){
        userService.createUser(user);
        return "redirect:/admin/users";
    }
    @GetMapping
    public String list(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "user/list";
    }
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        userService.removeUser(id);
        return "redirect:/admin/users";
    }
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model){
        Optional<User> userOpt = userService.findUserById(id);
        User user = userOpt.orElseThrow(() -> new RuntimeException("없음"));
        model.addAttribute("user", user);
        return "user/editForm";
    }
    @PostMapping("/edit/{id}")
    public String editUser(@ModelAttribute User user, @PathVariable Long id){
        userService.updateUser(id,user);
        return "redirect:/admin/users";
    }
}
package gift.controller;

import gift.model.User;
import gift.repository.UserDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserDao userDao;

    public AdminUserController(UserDao userDao) {
        this.userDao = userDao;
    }
    @GetMapping("/add")
    public String addForm(Model model){
        return "user/addForm";
    }
    @PostMapping("/add")
    public String addUser(@ModelAttribute User user){
        userDao.createUser(user);
        return "redirect:/admin/users";
    }
    @GetMapping
    public String list(Model model){
        model.addAttribute("users", userDao.getAllUsers());
        return "user/list";
    }
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        userDao.removeUser(id);
        return "redirect:/admin/users";
    }
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model){
        Optional<User> userOpt = userDao.findUserById(id);
        User user = userOpt.orElseThrow(() -> new RuntimeException("없음"));
        model.addAttribute("user", user);
        return "user/editForm";
    }
    @PostMapping("/edit/{id}")
    public String editUser(@ModelAttribute User user, @PathVariable Long id){
        userDao.updateUser(id,user);
        return "redirect:/admin/users";
    }
}
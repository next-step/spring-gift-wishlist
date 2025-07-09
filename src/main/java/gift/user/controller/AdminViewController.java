package gift.user.controller;

import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.user.dto.UserRequestDto;
import gift.user.dto.UserResponseDto;
import gift.user.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/admin")
public class AdminViewController {

  private UserService userService;

  public AdminViewController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public String index(Model model) {
    List<UserResponseDto> users = userService.findAllUsers();
    model.addAttribute("users", users);
    return "admin/index";
  }

  @GetMapping("/users/new")
  public String moveForm() {
    return "admin/form";
  }

  @PostMapping
  public String createUser(@RequestParam String email,@RequestParam String password) {
    UserRequestDto dto = new UserRequestDto(email,password);
    userService.saveUser(dto);
    return "redirect:/admin";
  }

  @GetMapping("/users/{id}")
  public String findUser(@PathVariable Long id, Model model) {
    UserResponseDto dto = userService.findById(id);
    model.addAttribute("product", dto);
    return "detail";
  }
  @GetMapping("/users/{id}/update")
  public String moveUpdateForm(@PathVariable Long id, Model model) {
    UserResponseDto user = userService.findById(id);
    model.addAttribute("user", user);
    return "update";
  }
  @PostMapping("/users/{id}")
  public String updateUser(@PathVariable Long id,@RequestParam String email,@RequestParam String password) {
    UserRequestDto dto = new UserRequestDto(email, password);
    userService.updateUser(id, dto);
    return "redirect:/admin/users" + id;
  }

  @GetMapping("/users/{id}/delete")
  public String deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return "redirect:/admin";
  }
}
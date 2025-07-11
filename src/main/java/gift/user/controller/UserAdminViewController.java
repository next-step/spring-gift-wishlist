package gift.user.controller;

import gift.user.domain.User;
import gift.user.dto.UserPatchRequestDto;
import gift.user.dto.UserSaveRequestDto;
import gift.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/admin/user")
public class UserAdminViewController {

    private final UserService userService;

    public UserAdminViewController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/list")
    public String findAll(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("userSaveRequestDto", new UserSaveRequestDto());
        return "userAddForm";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute UserSaveRequestDto userSaveRequestDto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return "userAddForm";
        }
        userService.save(userSaveRequestDto);
        return "redirect:/api/admin/user/list";
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable UUID id, Model model) {
        User user = userService.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
        UserPatchRequestDto userPatchRequestDto = new UserPatchRequestDto(user);
        model.addAttribute("userPatchRequestDto", userPatchRequestDto);
        return "userUpdateForm";
    }

    @PatchMapping("/{id}/update")
    public String update(@PathVariable UUID id, @Valid @ModelAttribute UserPatchRequestDto userPatchRequestDto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return "userUpdateForm";
        }
        userService.updateUser(id, userPatchRequestDto);
        return "redirect:/api/admin/user/list";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteById(@PathVariable UUID id) {
        userService.deleteUser(id);
        return "redirect:/api/admin/user/list";
    }

}

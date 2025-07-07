package gift.controller.view;

import gift.common.model.CustomPage;
import gift.entity.User;
import gift.service.user.UserService;
import jakarta.validation.constraints.Min;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/users")
public class UserViewController {
    private final UserService userService;

    public UserViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUserList(
        Model model,
        @RequestParam(value = "page", defaultValue = "0")
        @Min(value = 0, message = "페이지 번호는 0 이상이여야 합니다.") Integer page,
        @RequestParam(value = "size", defaultValue = "5")
        @Min(value = 1, message = "페이지 크기는 양수여야 합니다.") Integer size
    ) {
        CustomPage<User> currentPage = userService.getBy(page, size);
        model.addAttribute("title", "사용자 관리");
        model.addAttribute("pageInfo", currentPage);

        return "admin/user-list";
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("title", "사용자 등록");
        return "admin/create-user";
    }
}

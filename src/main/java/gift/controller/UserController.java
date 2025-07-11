package gift.controller;

import gift.DTO.LoginRequestDTO;
import gift.model.User;
import gift.repository.UserDao;
import gift.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public void registerUser(@RequestBody User user) {
        userService.createUser(user);
    }

    @PostMapping("/users/login")
    public String loginUser(@RequestBody LoginRequestDTO request) {
        return userService.login(request);
    }
}
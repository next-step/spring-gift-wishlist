package gift.controller.userController;


import gift.dto.itemDto.userDto.UserRegisterDto;
import gift.dto.itemDto.userDto.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/users")
public class UserController {

    @PostMapping()
    public ResponseEntity<UserResponseDto> registerUser(
            @RequestBody @Valid UserRegisterDto dto,
            Model model
    ) {
        UserResponseDto user =userService.registerUser(dto);
        return new ResponseEntity<UserResponseDto>(user, HttpStatus.CREATED);
    }
}

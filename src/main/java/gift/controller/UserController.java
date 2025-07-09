package gift.controller;

import gift.dto.user.UserRequestDto;
import gift.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequestDto userRequestDto) {
        if(userRepository.existsByEmail(userRequestDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 이메일입니다.");
        }

        String return_token = userRepository.save(userRequestDto).getToken();

        return ResponseEntity.status(HttpStatus.CREATED).body(return_token);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserRequestDto userRequestDto) {
        if(!userRepository.existsByEmail(userRequestDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("존재하지 않는 이메일입니다.");
        }

        if(!userRepository.checkPassword(userRequestDto.getEmail(),userRequestDto.getPassword())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("비밀번호가 일치하지 않습니다.");
        }
        String return_token = userRepository.access(userRequestDto).getToken();
        return ResponseEntity.status(HttpStatus.OK).body(return_token);
    }
}

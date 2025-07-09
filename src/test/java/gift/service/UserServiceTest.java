package gift.service;

import gift.dto.UserRequestDto;
import gift.entity.vo.Email;
import gift.entity.vo.Password;
import gift.exception.EmailAlreadyExistsException;
import gift.exception.InvalidLoginException;
import gift.repository.H2UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private H2UserRepository userRepository;

    UserRequestDto dto = new UserRequestDto(
            new Email("email@email.com"),
            new Password("12345678"));

    @BeforeEach
    void setUp() {
        userService.register(dto);
    }

    @Test
    @DisplayName("존재하는 이메일로 회원가입 시도 시 예외 발생")
    void test1() {
        assertThrows(EmailAlreadyExistsException.class, () -> userService.register(dto));
    }

    @Test
    @DisplayName("로그인 실패 시 예외 발생")
    void test2() {
        UserRequestDto dto1 = new UserRequestDto(
                new Email("email@email.com"),
                new Password("1234567890")
        );
        assertThrows(InvalidLoginException.class, () -> userService.login(dto1));
    }
}
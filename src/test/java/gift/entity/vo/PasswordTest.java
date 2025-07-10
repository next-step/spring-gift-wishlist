package gift.entity.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    @Test
    @DisplayName("비밀번호 검증 시 예외 발생")
    void invalidPasswordExceptionTest() {
        assertThrows(IllegalArgumentException.class, () -> new Password("1234567"));
    }

    @Test
    @DisplayName("비밀번호 matches 성공")
    void passwordMatchTest() {
        Password pw1 = new Password("123456789");
        Password pw2 = new Password("123456789");
        assertTrue(pw1.matches(pw2));
    }

}
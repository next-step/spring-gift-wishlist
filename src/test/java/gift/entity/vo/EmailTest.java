package gift.entity.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    @DisplayName("이메일 검증 시 예외 발생")
    void invalidEmailExceptionTest() {
        assertThrows(IllegalArgumentException.class, () -> new Email("invalid_email"));
    }
}
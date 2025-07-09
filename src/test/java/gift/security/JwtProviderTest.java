package gift.security;

import gift.entity.User;
import gift.entity.vo.Email;
import gift.entity.vo.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtProviderTest {

    private final String secret = "01234567890123456789012345678901";
    private JwtProvider jwtProvider = new JwtProvider(secret);;
    private User testUser;
    private String token;

    @BeforeEach
    void setUp() {
        testUser = new User(
                new Email("email@email.com"),
                new Password("12345678")
        );
        testUser.setId(1L);
        token = jwtProvider.generateToken(testUser);
    }


    @Test
    @DisplayName("invalid 토큰 검증")
    void test1() {
        String invalidToken = "invalid_token";
        assertFalse(jwtProvider.validateToken(invalidToken));
    }

    @Test
    @DisplayName("get 유저 id")
    void test2() {
        assertEquals(1L, jwtProvider.getUserId(token));
    }

    @Test
    @DisplayName("만료되지 않은 토큰 확인")
    void test3() {
        assertFalse(jwtProvider.isTokenExpired(token));
    }

}
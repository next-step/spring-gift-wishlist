package gift.auth;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        // 테스트용 설정값
        String secretKey = "test-secret-key-for-jwt-token-provider-unit-test-must-be-at-least-256-bits";
        long expirationTime = 3600000; // 1시간

        jwtTokenProvider = new JwtTokenProvider(secretKey, expirationTime);
    }

    @Test
    @DisplayName("회원 ID로 JWT 토큰을 생성한다")
    void createToken_success() {
        // given
        Long memberId = 5L;

        // when
        String token = jwtTokenProvider.createToken(memberId);

        // then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3); // JWT는 3부분으로 구성
    }

    @Test
    @DisplayName("생성한 토큰에서 회원 ID를 추출할 수 있다")
    void getMemberId_success() {
        // given
        Long expectedMemberId = 5L;
        String token = jwtTokenProvider.createToken(expectedMemberId);

        // when
        Long actualMemberId = jwtTokenProvider.getMemberId(token);

        // then
        assertThat(actualMemberId).isEqualTo(expectedMemberId);
    }

    @Test
    @DisplayName("유효한 토큰은 검증을 통과한다")
    void validateToken_validToken_returnsTrue() {
        // given
        String token = jwtTokenProvider.createToken(5L);

        // when
        boolean isValid = jwtTokenProvider.validateToken(token);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("null 토큰은 검증을 통과하지 못한다")
    void validateToken_nullToken_returnsFalse() {
        // when
        boolean isValid = jwtTokenProvider.validateToken(null);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("빈 토큰은 검증을 통과하지 못한다")
    void validateToken_emptyToken_returnsFalse() {
        // when
        boolean isValid = jwtTokenProvider.validateToken("");

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("잘못된 형식의 토큰은 검증을 통과하지 못한다")
    void validateToken_malformedToken_returnsFalse() {
        // given
        String malformedToken = "this.is.not.a.valid.jwt.token";

        // when
        boolean isValid = jwtTokenProvider.validateToken(malformedToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("다른 키로 서명된 토큰은 검증을 통과하지 못한다")
    void validateToken_differentKey_returnsFalse() {
        // given
        JwtTokenProvider otherProvider = new JwtTokenProvider(
                "different-secret-key-that-is-long-enough-for-jwt-requirements-256-bits",
                3600000
        );
        String tokenFromOtherProvider = otherProvider.createToken(5L);

        // when
        boolean isValid = jwtTokenProvider.validateToken(tokenFromOtherProvider);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("만료된 토큰은 검증을 통과하지 못한다")
    void validateToken_expiredToken_returnsFalse() throws InterruptedException {
        // given
        JwtTokenProvider shortLivedProvider = new JwtTokenProvider(
                "test-secret-key-for-jwt-token-provider-unit-test-must-be-at-least-256-bits",
                1 // 1밀리초
        );
        String token = shortLivedProvider.createToken(5L);

        // 토큰 만료 대기
        Thread.sleep(10);

        // when
        boolean isValid = shortLivedProvider.validateToken(token);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("잘못된 토큰에서 회원 ID를 추출하면 예외가 발생한다")
    void getMemberId_invalidToken_throwsException() {
        // given
        String invalidToken = "invalid.jwt.token";

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.getMemberId(invalidToken))
                .isInstanceOf(JwtException.class);
    }
}
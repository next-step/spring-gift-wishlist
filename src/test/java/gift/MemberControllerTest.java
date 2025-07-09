package gift;

import static org.assertj.core.api.Assertions.assertThat;

import gift.common.dto.ErrorResponseDto;
import gift.exception.ErrorStatus;
import gift.member.dto.AccessTokenRefreshRequestDto;
import gift.member.dto.AccessTokenRefreshResponseDto;
import gift.member.dto.MemberDto;
import gift.member.dto.MemberLoginRequestDto;
import gift.member.dto.MemberLoginResponseDto;
import gift.member.dto.MemberRegisterRequestDto;
import gift.token.service.TokenProvider;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    TokenProvider tokenProvider;

    private String baseUrl;

    private static final String VALID_PW = "qwerty12345678";
    private static final String VALID_NAME = "양준영";

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/members";
    }


    private String generateUniqueValidEmail() {
        return "test_" + UUID.randomUUID() + "@example.com";
    }

    @Test
    void 회원가입_성공() {
        var requestDto = new MemberRegisterRequestDto(generateUniqueValidEmail(), VALID_NAME,
                VALID_PW);
        var response = restTemplate.postForEntity(baseUrl, requestDto, MemberDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().email()).isEqualTo(requestDto.email());
        assertThat(response.getBody().name()).isEqualTo(requestDto.name());
    }

    @Test
    void 회원가입_실패_중복이메일() {
        var requestDto1 = new MemberRegisterRequestDto(generateUniqueValidEmail(), VALID_NAME,
                VALID_PW);
        restTemplate.postForEntity(baseUrl, requestDto1, MemberRegisterRequestDto.class);

        var requestDto2 = new MemberRegisterRequestDto(requestDto1.email(), "홍길동", "qweqweqweqwe");
        var response = restTemplate.postForEntity(baseUrl, requestDto2,
                ErrorResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().statusCode()).isEqualTo(
                ErrorStatus.ENTITY_ALREADY_EXISTS.getCode());
    }

    @Test
    void 회원가입_실패_이메일_형식_오류() {
        var requestDto = new MemberRegisterRequestDto("a   @a   .com", VALID_NAME, VALID_PW);
        var response = restTemplate.postForEntity(baseUrl, requestDto, ErrorResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().statusCode()).isEqualTo(
                ErrorStatus.VALIDATION_ERROR.getCode());
    }

    @Test
    void 회원가입_실패_비밀번호_길이_부족() {
        var requestDto = new MemberRegisterRequestDto(generateUniqueValidEmail(), VALID_NAME, "1234567");
        var response = restTemplate.postForEntity(baseUrl, requestDto, ErrorResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().statusCode()).isEqualTo(
                ErrorStatus.VALIDATION_ERROR.getCode());
    }

    @Test
    void 로그인_성공() {
        var registerRequestDto = new MemberRegisterRequestDto(generateUniqueValidEmail(),
                VALID_NAME, VALID_PW);
        restTemplate.postForEntity(baseUrl, registerRequestDto, MemberRegisterRequestDto.class);

        var loginRequestDto = new MemberLoginRequestDto(registerRequestDto.email(),
                registerRequestDto.password());
        var loginResponse = restTemplate.postForEntity(baseUrl + "/login", loginRequestDto,
                MemberLoginResponseDto.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(loginResponse.getBody()).isNotNull();

        assertThat(loginResponse.getBody().memberDto()).isNotNull();
        assertThat(loginResponse.getBody().memberDto().uuid()).isNotNull();
        assertThat(loginResponse.getBody().memberDto().email()).isEqualTo(
                registerRequestDto.email());
        assertThat(loginResponse.getBody().memberDto().name()).isEqualTo(
                registerRequestDto.name());

        assertThat(loginResponse.getBody().tokenInfo()).isNotNull();
        assertThat(loginResponse.getBody().tokenInfo().accessToken()).isNotBlank();
        assertThat(loginResponse.getBody().tokenInfo().refreshToken()).isNotBlank();

        assertThat(tokenProvider.getMemberUuidFromAccessToken(
                loginResponse.getBody().tokenInfo().accessToken()))
                .isEqualTo(loginResponse.getBody().memberDto().uuid());
    }

    @Test
    void 로그인_실패_이메일_오류() {
        var registerRequestDto = new MemberRegisterRequestDto(generateUniqueValidEmail(),
                VALID_NAME, VALID_PW);
        restTemplate.postForEntity(baseUrl, registerRequestDto, MemberRegisterRequestDto.class);

        var loginRequestDto = new MemberLoginRequestDto(generateUniqueValidEmail(),
                registerRequestDto.password());
        var loginResponse = restTemplate.postForEntity(baseUrl + "/login", loginRequestDto,
                ErrorResponseDto.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(loginResponse.getBody()).isNotNull();
        assertThat(loginResponse.getBody().statusCode()).isEqualTo(
                ErrorStatus.INVALID_CREDENTIALS.getCode());
    }

    @Test
    void 로그인_실패_비밀번호_오류() {
        var registerRequestDto = new MemberRegisterRequestDto(generateUniqueValidEmail(),
                VALID_NAME, VALID_PW);
        restTemplate.postForEntity(baseUrl, registerRequestDto, MemberRegisterRequestDto.class);

        var loginRequestDto = new MemberLoginRequestDto(registerRequestDto.email(), "PASSWORD!@");
        var loginResponse = restTemplate.postForEntity(baseUrl + "/login", loginRequestDto,
                ErrorResponseDto.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(loginResponse.getBody()).isNotNull();
        assertThat(loginResponse.getBody().statusCode()).isEqualTo(
                ErrorStatus.INVALID_CREDENTIALS.getCode());
    }

    @Test
    void 로그인_실패_이메일_비밀번호_오류() {
        var registerRequestDto = new MemberRegisterRequestDto(generateUniqueValidEmail(),
                VALID_NAME, VALID_PW);
        restTemplate.postForEntity(baseUrl, registerRequestDto, MemberRegisterRequestDto.class);

        var loginRequestDto = new MemberLoginRequestDto(generateUniqueValidEmail(), "PASSWORD!@");
        var loginResponse = restTemplate.postForEntity(baseUrl + "/login", loginRequestDto,
                ErrorResponseDto.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(loginResponse.getBody()).isNotNull();
        assertThat(loginResponse.getBody().statusCode()).isEqualTo(
                ErrorStatus.INVALID_CREDENTIALS.getCode());
    }

    @Test
    void AccessToken_재발급_성공() {
        var registerRequestDto = new MemberRegisterRequestDto(generateUniqueValidEmail(),
                VALID_NAME, VALID_PW);
        restTemplate.postForEntity(baseUrl, registerRequestDto, MemberRegisterRequestDto.class);

        var loginRequestDto = new MemberLoginRequestDto(registerRequestDto.email(),
                registerRequestDto.password());
        var loginResponse = restTemplate.postForEntity(baseUrl + "/login", loginRequestDto,
                MemberLoginResponseDto.class);

        var refreshAccessTokenRequestDto = new AccessTokenRefreshRequestDto(
                loginResponse.getBody().tokenInfo().refreshToken());
        var refreshResponse = restTemplate.postForEntity(baseUrl + "/refresh",
                refreshAccessTokenRequestDto, AccessTokenRefreshResponseDto.class);

        assertThat(refreshResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(refreshResponse.getBody()).isNotNull();
        assertThat(tokenProvider.getMemberUuidFromAccessToken(refreshResponse.getBody().accessToken()))
                .isEqualTo(loginResponse.getBody().memberDto().uuid());

    }

    @Test
    void AccessToken_재발급_실패_RefreshToken_오류() {
        var registerRequestDto = new MemberRegisterRequestDto(generateUniqueValidEmail(),
                VALID_NAME, VALID_PW);
        restTemplate.postForEntity(baseUrl, registerRequestDto, MemberRegisterRequestDto.class);

        var loginRequestDto = new MemberLoginRequestDto(registerRequestDto.email(),
                registerRequestDto.password());
        restTemplate.postForEntity(baseUrl + "/login", loginRequestDto,
                MemberLoginResponseDto.class);

        var refreshAccessTokenRequestDto = new AccessTokenRefreshRequestDto(
                UUID.randomUUID().toString().replace("-", ""));
        var refreshResponse = restTemplate.postForEntity(baseUrl + "/refresh",
                refreshAccessTokenRequestDto, ErrorResponseDto.class);

        assertThat(refreshResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(refreshResponse.getBody()).isNotNull();
        assertThat(refreshResponse.getBody().statusCode()).isEqualTo(
                ErrorStatus.INVALID_CREDENTIALS.getCode());
    }
}

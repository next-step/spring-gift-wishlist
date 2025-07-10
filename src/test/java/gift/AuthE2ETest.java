package gift;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gift.api.dto.MemberRequestDto;
import gift.api.dto.TokenResponseDto;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthE2ETest {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @Autowired
    private JdbcClient jdbcClient;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String ADMIN_EMAIL = "admin@test.com";
    private static final String USER_EMAIL = "user@test.com";
    private static final String PASSWORD = "password123";

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();

        String encodedPassword = passwordEncoder.encode(PASSWORD);

        jdbcClient.sql(
                        "insert into member(email, password, role) values (:email, :password, :role)")
                .param("email", ADMIN_EMAIL)
                .param("password", encodedPassword)
                .param("role", "ADMIN")
                .update();

        jdbcClient.sql(
                        "insert into member(email, password, role) values (:email, :password, :role)")
                .param("email", USER_EMAIL)
                .param("password", encodedPassword)
                .param("role", "USER")
                .update();
    }

    @AfterEach
    void tearDown() {
        jdbcClient.sql("delete from member").update();
        jdbcClient.sql("delete from product").update();
    }

    @Test
    void 관리자_로그인_성공() {
        MemberRequestDto request = new MemberRequestDto(ADMIN_EMAIL, PASSWORD);

        TokenResponseDto response = restClient.post()
                .uri("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(TokenResponseDto.class);

        assertNotNull(response);
        assertNotNull(response.token());
    }

    @Test
    void 관리자_페이지_접근_성공() {
        MemberRequestDto request = new MemberRequestDto(ADMIN_EMAIL, PASSWORD);

        TokenResponseDto tokenResponse = restClient.post()
                .uri("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(TokenResponseDto.class);

        String token = Objects.requireNonNull(tokenResponse).token();

        String response = restClient.get()
                .uri("/admin/products")
                .header("Authorization", token)
                .retrieve()
                .body(String.class);

        assertNotNull(response);
        assertTrue(response.contains("상품 등록"));
    }

    @Test
    void 일반_사용자_관리자_페이지_접근_실패() {
        MemberRequestDto request = new MemberRequestDto(USER_EMAIL, PASSWORD);

        TokenResponseDto tokenResponse = restClient.post()
                .uri("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(TokenResponseDto.class);

        String token = Objects.requireNonNull(tokenResponse).token();

        String responseBody = restClient.get()
                .uri("/admin/products/new")
                .header("Authorization", token)
                .retrieve()
                .body(String.class);

        assertNotNull(responseBody);
        assertTrue(responseBody.contains("관리자 권한이 필요한 페이지입니다."));
        assertTrue(responseBody.contains("오류가 발생했습니다"));
    }

    @Test
    void 토큰_없이_페이지_접근_실패() {
        String responseBody = restClient.get()
                .uri("/members/products")
                .retrieve()
                .body(String.class);

        assertNotNull(responseBody);
        assertTrue(responseBody.contains("로그인이 필요한 페이지입니다."));
        assertTrue(responseBody.contains("오류가 발생했습니다"));
    }
}

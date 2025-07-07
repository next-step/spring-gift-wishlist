package gift;

import gift.dto.CreateMemberRequest;
import gift.dto.CreateMemberResponse;
import gift.dto.LoginMemberRequest;
import gift.dto.LoginMemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/clear_member_table.sql")
class MemberControllerTest {

    private final String baseUrl = "http://localhost:";

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create();
    }

    @Test
    void 회원가입_성공() throws Exception {
        String url = baseUrl + port + "/api/members";
        CreateMemberRequest request = new CreateMemberRequest("test@example.com", "password123456789");

        ResponseEntity<CreateMemberResponse> response = restClient.post()
                .uri(url)
                .body(request)
                .retrieve()
                .toEntity(CreateMemberResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).hasFieldOrProperty("token");
    }

    @Test
    void 회원가입_실패_이메일_중복() throws Exception {
        String url = baseUrl + port + "/api/members";

        // 먼저 회원가입을 수행
        CreateMemberRequest request = new CreateMemberRequest("existing@example.com", "password123456789");
        restClient.post()
                .uri(url)
                .body(request)
                .retrieve()
                .toBodilessEntity();

        // 동일한 이메일로 두 번째 회원가입 시도
        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(() -> restClient.post()
                        .uri(url)
                        .body(request)
                        .retrieve()
                        .toBodilessEntity())
                .satisfies(ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    void 로그인_성공() throws Exception {
        String memberUrl = baseUrl + port + "/api/members";
        String loginUrl = baseUrl + port + "/api/members/login";

        // 먼저 회원가입을 수행
        CreateMemberRequest createRequest = new CreateMemberRequest("existing@example.com", "password123456789");
        restClient.post()
                .uri(memberUrl)
                .body(createRequest)
                .retrieve()
                .toBodilessEntity();

        // 생성된 회원으로 로그인
        LoginMemberRequest loginRequest = new LoginMemberRequest("existing@example.com", "password123456789");
        ResponseEntity<LoginMemberResponse> response = restClient.post()
                .uri(loginUrl)
                .body(loginRequest)
                .retrieve()
                .toEntity(LoginMemberResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasFieldOrProperty("token");
    }

    @Test
    void 로그인_실패_잘못된_비밀번호() throws Exception {
        String memberUrl = baseUrl + port + "/api/members";
        String loginUrl = baseUrl + port + "/api/members/login";

        // 먼저 회원가입을 수행
        CreateMemberRequest createRequest = new CreateMemberRequest("existing@example.com", "password123456789");
        restClient.post()
                .uri(memberUrl)
                .body(createRequest)
                .retrieve()
                .toBodilessEntity();

        // 생성된 회원의 이메일로 잘못된 비밀번호로 로그인 시도
        LoginMemberRequest loginRequest = new LoginMemberRequest("existing@example.com", "wrongpassword");

        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(() -> restClient.post()
                        .uri(loginUrl)
                        .body(loginRequest)
                        .retrieve()
                        .toBodilessEntity())
                .satisfies(ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN));
    }

    @Test
    void 회원가입_실패_짧은_비밀번호() throws Exception {
        String url = baseUrl + port + "/api/members";
        CreateMemberRequest request = new CreateMemberRequest("test@example.com", "short");

        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(() -> restClient.post()
                        .uri(url)
                        .body(request)
                        .retrieve()
                        .toBodilessEntity())
                .satisfies(ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    void 회원가입_실패_잘못된_이메일_형식() throws Exception {
        String url = baseUrl + port + "/api/members";
        CreateMemberRequest request = new CreateMemberRequest("invalid-email", "password123456789");

        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(() -> restClient.post()
                        .uri(url)
                        .body(request)
                        .retrieve()
                        .toBodilessEntity())
                .satisfies(ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST));
    }
}

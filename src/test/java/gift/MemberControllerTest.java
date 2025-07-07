package gift;

import static org.assertj.core.api.Assertions.assertThat;

import gift.jwt.JwtResponse;
import gift.member.domain.enums.UserRole;
import gift.member.dto.LogInRequest;
import gift.member.dto.MemberResponse;
import gift.member.dto.RegisterRequest;
import gift.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    String baseURL;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        baseURL = "http://localhost:" + port + "/api/members";
        restClient = RestClient.builder().baseUrl(baseURL).build();
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    void 회원가입_테스트() {
        // given
        RegisterRequest registerReqDTO = new RegisterRequest(
            "asdf@gmail.com",
            "test1234",
            UserRole.NORMAL
        );

        // when
        var response = restClient.post()
            .uri("/register")
            .body(registerReqDTO)
            .retrieve()
            .toEntity(JwtResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        JwtResponse jwtResponse = response.getBody();
        assertThat(jwtResponse).isNotNull();
        assertThat(jwtResponse.id()).isEqualTo(1L);
    }

    @Test
    void 로그인_테스트() {
        회원가입_테스트();

        // given
        LogInRequest logInReqDTO = new LogInRequest(
            "asdf@gmail.com",
            "test1234"
        );

        // when
        var response = restClient.post()
            .uri("/login")
            .body(logInReqDTO)
            .retrieve()
            .toEntity(JwtResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JwtResponse jwtResponse = response.getBody();
        assertThat(jwtResponse).isNotNull();
        assertThat(jwtResponse.id()).isEqualTo(1L);
    }

    @Test
    void 회원정보_조회_테스트() {
        // given
        String email = "asdf@gmail.com";
        String password = "test1234";
        회원가입(email, password, UserRole.NORMAL);
        String accessToken = 토큰_발급(email, password);

        // when
        var response = restClient.get()
            .headers(headers -> headers.setBearerAuth(accessToken))
            .retrieve()
            .toEntity(MemberResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        MemberResponse memberResponse = response.getBody();
        assertThat(memberResponse).isNotNull();
        assertThat(memberResponse.id()).isEqualTo(1L);
        assertThat(memberResponse.email()).isEqualTo(email);
    }

    private void 회원가입(String email, String password, UserRole role) {
        RegisterRequest registerReqDTO = new RegisterRequest(
            email,
            password,
            role
        );

        restClient.post()
            .uri("/register")
            .body(registerReqDTO)
            .retrieve()
            .toEntity(JwtResponse.class);
    }

    private String 토큰_발급(String email, String password) {
        LogInRequest logInReqDTO = new LogInRequest(email, password);

        var response = restClient.post()
            .uri("/login")
            .body(logInReqDTO)
            .retrieve()
            .toEntity(JwtResponse.class);

        return response.getBody().accessToken();
    }

}

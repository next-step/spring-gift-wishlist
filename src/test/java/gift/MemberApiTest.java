package gift;

import static org.assertj.core.api.Assertions.assertThat;

import gift.dto.jwt.TokenResponse;
import gift.dto.member.LoginRequest;
import gift.dto.member.MemberRequest;
import gift.dto.member.RegisterResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("일반 사용자용 JWT API 테스트")
public class MemberApiTest {

    private final RestClient restClient = RestClient.builder().build();

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 회원가입하면_201_반환() {
        String url = "http://localhost:" + port + "/api/members/register";
        MemberRequest memberRequest = new MemberRequest("test@test", "test");

        var response = restClient.post()
            .uri(url)
            .body(memberRequest)
            .retrieve()
            .toEntity(RegisterResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 로그인하면_200_반환() {
        String url = "http://localhost:" + port + "/api/members/login";
        LoginRequest loginRequest = new LoginRequest("member1", "password1");

        var response = restClient.post()
            .uri(url)
            .body(loginRequest)
            .retrieve()
            .toEntity(TokenResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}

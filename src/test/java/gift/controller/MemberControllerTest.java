package gift.controller;

import static org.assertj.core.api.Assertions.*;

import gift.dto.MemberRequestDto;
import gift.entity.Product;
import gift.repository.MemberRepositoryImpl;
import gift.service.JwtAuthService;
import gift.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.client.HttpClientProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

    @LocalServerPort
    private int port;

    private RestClient restClient = RestClient.builder().build();
    @Autowired
    private HttpClientProperties httpClientProperties;

    @Test
    void 회원_가입_성공시_토큰이_반환() {
        var url = "http://localhost:" + port + "/api/members/register";

        MemberRequestDto member1 = new MemberRequestDto("abc123@gmail.com", "password");

        var response = restClient.post()
                .uri(url)
                .body(member1)
                .retrieve()
                .toEntity(String.class);

        System.out.println("createdToken = " + response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 중복되는_이메일은_회원가입_불가() {
        var url = "http://localhost:" + port + "/api/members/register";

        MemberRequestDto member1 = new MemberRequestDto("abcd123@gmail.com", "password");

        var response1 = restClient.post()
                .uri(url)
                .body(member1)
                .retrieve()
                .toEntity(String.class);

        System.out.println("createdToken = " + response1.getBody());
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        MemberRequestDto member2 = new MemberRequestDto("abcd123@gmail.com", "password");
        Assertions.assertThrows(HttpClientErrorException.BadRequest.class,
                () -> restClient.post()
                        .uri(url)
                        .body(member2)
                        .retrieve()
                        .toEntity(String.class)
        );
    }

}
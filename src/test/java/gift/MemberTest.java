package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberTest {

    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();

    @Autowired
    private JdbcClient jdbcClient;

    @AfterEach
    void cleanUp() {
        jdbcClient.sql("TRUNCATE TABLE member RESTART IDENTITY").update();
    }

    @Test
    void 회원가입_성공() {
        String url = "http://localhost:" + port + "/api/members/register";
        MemberRequestDto memberRequestDto = new MemberRequestDto("user@email.com", "password");

        ResponseEntity<TokenResponseDto> responseEntity = client.post()
                                                                .uri(url)
                                                                .body(memberRequestDto)
                                                                .retrieve()
                                                                .toEntity(
                                                                        TokenResponseDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody().token()).isNotNull();
    }

    @Test
    void 이메일_중복으로_회원가입_실패() {
        String url = "http://localhost:" + port + "/api/members/register";
        jdbcClient.sql(
                          "INSERT INTO member (email, password, role) VALUES ('user@email.com', 'password', 'ROLE_USER')")
                  .update();

        MemberRequestDto memberRequestDto = new MemberRequestDto("user@email.com", "password");

        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.Conflict.class,
                () -> client.post()
                            .uri(url)
                            .body(memberRequestDto)
                            .retrieve()
                            .toBodilessEntity()
        );

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

}

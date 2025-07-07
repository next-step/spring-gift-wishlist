package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.dto.LoginRequestDTO;
import gift.dto.RegisterRequestDTO;
import gift.dto.TokenResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MemberControllerTest {
    @LocalServerPort
    private int port;
    private RestClient client;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        String url = "http://localhost:" + port + "/api/members";
        client = RestClient.builder().baseUrl(url).build();
    }

    @BeforeEach
    void clearDatabase() {
        jdbcTemplate.update("DELETE FROM member");
    }

    @Test
    @DisplayName("회원가입 - 성공")
    void registerTest() {
        final String email = "test@test.com";
        final String password = "password123";
        RegisterRequestDTO req = new RegisterRequestDTO();
        req.setEmail(email);
        req.setPassword(password);

        ResponseEntity<TokenResponseDTO> response = client.post()
            .uri("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .body(req)
            .retrieve()
            .toEntity(TokenResponseDTO.class);

        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().token()).isNotBlank();
    }

    @Test
    @DisplayName("로그인 - 성공")
    void loginTest() {
        final String email = "test@test.com";
        final String password = "password123";
        RegisterRequestDTO req = new RegisterRequestDTO();
        req.setEmail(email);
        req.setPassword(password);

        client.post().uri("/register").contentType(MediaType.APPLICATION_JSON).body(req).retrieve().toBodilessEntity();

        ResponseEntity<TokenResponseDTO> response = client.post()
            .uri("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(req)
            .retrieve()
            .toEntity(TokenResponseDTO.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().token()).isNotBlank();
    }

    @Test
    @DisplayName("로그인 - 실패")
    void loginTestNoEmail() {
        final String email = "test@test.com";
        final String password = "password123";
        LoginRequestDTO req = new LoginRequestDTO();
        req.setEmail(email);
        req.setPassword(password);

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
            client.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(req)
                .retrieve()
                .toEntity(TokenResponseDTO.class)
        );
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}

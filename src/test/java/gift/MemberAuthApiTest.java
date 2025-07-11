package gift;

import gift.dto.MemberRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberAuthApiTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void jwt_인증_실패시_401반환() {
        // 토큰 없이 인증이 필요한 API 호출
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/api/products", String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).contains("인증이 필요");
    }

    @Test
    void 로그인_실패시_403반환() {
        // 회원가입
        Map<String, String> req = Map.of("email", "test@fail.com", "password", "123456");
        restTemplate.postForEntity(baseUrl + "/api/members/register", req, String.class);

        // 잘못된 비밀번호로 로그인
        Map<String, String> loginReq = Map.of("email", "test@fail.com", "password", "wrongpass");
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/api/members/login", loginReq, String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).contains("비밀번호");
    }
} 
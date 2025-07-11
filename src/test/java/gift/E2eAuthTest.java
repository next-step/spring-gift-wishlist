package gift;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class E2eAuthTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void 회원가입_로그인_인증_전체_플로우_테스트() {
        String baseUrl = "http://localhost:" + port;

        // 1. 회원가입
        Map<String, String> registerRequest = Map.of(
            "email", "testuser@example.com",
            "password", "123456"
        );
        ResponseEntity<Map> registerResponse = restTemplate.postForEntity(
            baseUrl + "/api/members/register", registerRequest, Map.class
        );
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String token = (String) registerResponse.getBody().get("token");
        assertThat(token).isNotBlank();

        // 2. 토큰 없이 인증이 필요한 API 호출 → 401
        ResponseEntity<String> noAuthResponse = restTemplate.getForEntity(
            baseUrl + "/admin/members", String.class
        );
        assertThat(noAuthResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // 3. 토큰을 헤더에 넣고 인증이 필요한 API 호출 → 200
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> authResponse = restTemplate.exchange(
            baseUrl + "/admin/members", HttpMethod.GET, entity, String.class
        );
        assertThat(authResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
} 
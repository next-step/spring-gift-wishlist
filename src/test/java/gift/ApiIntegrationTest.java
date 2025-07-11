package gift;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiIntegrationTest {

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
    void 회원가입_로그인_토큰인증_전체플로우() {
        // 1. 회원가입 (매번 다른 이메일 사용)
        String uniqueEmail = "test" + UUID.randomUUID().toString().substring(0, 8) + "@integration.com";
        Map<String, String> registerRequest = Map.of("email", uniqueEmail, "password", "123456");
        ResponseEntity<String> registerResponse = restTemplate.postForEntity(
            baseUrl + "/api/members/register", 
            registerRequest, 
            String.class
        );
        
        System.out.println("=== 회원가입 응답 ===");
        System.out.println("상태 코드: " + registerResponse.getStatusCode());
        System.out.println("응답 본문: " + registerResponse.getBody());
        
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(registerResponse.getBody()).contains("token");

        // 2. 로그인
        Map<String, String> loginRequest = Map.of("email", uniqueEmail, "password", "123456");
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
            baseUrl + "/api/members/login", 
            loginRequest, 
            String.class
        );
        
        System.out.println("=== 로그인 응답 ===");
        System.out.println("상태 코드: " + loginResponse.getStatusCode());
        System.out.println("응답 본문: " + loginResponse.getBody());
        
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).contains("token");

        // 3. 토큰 추출 (간단한 방법)
        String token = loginResponse.getBody().split("\"token\":\"")[1].split("\"")[0];
        System.out.println("추출된 토큰: " + token);

        // 4. 토큰으로 인증이 필요한 API 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<String> productsResponse = restTemplate.exchange(
            baseUrl + "/api/products",
            HttpMethod.GET,
            entity,
            String.class
        );
        
        System.out.println("=== 인증된 API 호출 응답 ===");
        System.out.println("상태 코드: " + productsResponse.getStatusCode());
        System.out.println("응답 본문: " + productsResponse.getBody());
        
        assertThat(productsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 잘못된_토큰으로_접근시_401반환() {
        // 잘못된 토큰으로 API 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("invalid.token.here");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/api/products",
            HttpMethod.GET,
            entity,
            String.class
        );
        
        System.out.println("=== 잘못된 토큰 테스트 ===");
        System.out.println("상태 코드: " + response.getStatusCode());
        System.out.println("응답 본문: " + response.getBody());
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void 토큰없이_접근시_401반환() {
        // 토큰 없이 API 호출
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/api/products", String.class);
        
        System.out.println("=== 토큰 없음 테스트 ===");
        System.out.println("상태 코드: " + response.getStatusCode());
        System.out.println("응답 본문: " + response.getBody());
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).contains("인증이 필요");
    }

    @Test
    void 로그인_실패시_403반환() {
        // 먼저 회원가입 (매번 다른 이메일 사용)
        String uniqueEmail = "fail" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
        Map<String, String> registerRequest = Map.of("email", uniqueEmail, "password", "123456");
        restTemplate.postForEntity(baseUrl + "/api/members/register", registerRequest, String.class);

        // 잘못된 비밀번호로 로그인
        Map<String, String> loginRequest = Map.of("email", uniqueEmail, "password", "wrongpass");
        ResponseEntity<String> response = restTemplate.postForEntity(
            baseUrl + "/api/members/login", 
            loginRequest, 
            String.class
        );
        
        System.out.println("=== 로그인 실패 테스트 ===");
        System.out.println("상태 코드: " + response.getStatusCode());
        System.out.println("응답 본문: " + response.getBody());
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).contains("비밀번호");
    }

    @Test
    void 일반사용자는_어드민경로_접근시_403() {
        // 1. 일반 사용자 회원가입 및 로그인
        String userEmail = "user" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
        Map<String, String> registerReq = Map.of("email", userEmail, "password", "123456");
        restTemplate.postForEntity(baseUrl + "/api/members/register", registerReq, String.class);

        Map<String, String> loginReq = Map.of("email", userEmail, "password", "123456");
        ResponseEntity<String> loginRes = restTemplate.postForEntity(baseUrl + "/api/members/login", loginReq, String.class);
        String token = loginRes.getBody().split("\"token\":\"")[1].split("\"")[0];

        // 2. 어드민 경로 접근
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/admin/products", // 어드민만 접근 가능한 경로
            HttpMethod.GET,
            entity,
            String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
} 
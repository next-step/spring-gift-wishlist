package gift;

import gift.dto.WishRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class WishlistApiIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    String baseUrl;

    String userToken;
    String adminToken;
    Long productId;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;

        // 1. 상품 등록 (관리자 계정)
        // 관리자 로그인
        Map<String, String> adminLogin = Map.of("email", "admin@admin", "password", "admin123");
        ResponseEntity<Map> adminLoginRes = restTemplate.postForEntity(baseUrl + "/api/members/login", adminLogin, Map.class);
        adminToken = (String) adminLoginRes.getBody().get("token");

        productId = 1L;

        // 2. 일반 사용자 회원가입 및 로그인
        String userEmail = "user" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
        Map<String, String> registerReq = Map.of("email", userEmail, "password", "123456");
        restTemplate.postForEntity(baseUrl + "/api/members/register", registerReq, String.class);

        Map<String, String> loginReq = Map.of("email", userEmail, "password", "123456");
        ResponseEntity<Map> loginRes = restTemplate.postForEntity(baseUrl + "/api/members/login", loginReq, Map.class);
        userToken = (String) loginRes.getBody().get("token");
    }

    @Test
    @DisplayName("위시리스트 추가/조회/수정/삭제/예외처리 통합 테스트")
    void wishlist_flow_and_exceptions() {
        // 1. 인증 없이 위시리스트 추가 → 401
        WishRequestDto wishReq = new WishRequestDto(productId, 2);
        ResponseEntity<String> noAuthRes = restTemplate.postForEntity(baseUrl + "/wishlist", wishReq, String.class);
        assertThat(noAuthRes.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // 2. 정상적으로 위시리스트 추가 → 201
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(userToken);
        HttpEntity<WishRequestDto> addEntity = new HttpEntity<>(wishReq, userHeaders);
        ResponseEntity<String> addRes = restTemplate.exchange(baseUrl + "/wishlist", HttpMethod.POST, addEntity, String.class);
        assertThat(addRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(addRes.getBody()).contains("\"product\":{\"id\":" + productId);

        // 3. 중복 추가 → 400, "이미 위시리스트에 추가된 상품입니다."
        ResponseEntity<String> dupRes = restTemplate.exchange(baseUrl + "/wishlist", HttpMethod.POST, addEntity, String.class);
        assertThat(dupRes.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(dupRes.getBody()).contains("이미 위시리스트에 추가된 상품입니다.");

        // 4. 위시리스트 전체 조회 → 200, 본인 것만
        HttpEntity<Void> getEntity = new HttpEntity<>(userHeaders);
        ResponseEntity<String> getRes = restTemplate.exchange(baseUrl + "/wishlist", HttpMethod.GET, getEntity, String.class);
        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getRes.getBody()).contains("\"product\":{\"id\":" + productId);

        // 5. 수량 변경 → 200
        // 먼저 위시리스트 id 추출
        String body = getRes.getBody();
        Long wishId = Long.valueOf(body.split("\"id\":")[1].split(",")[0].trim());
        WishRequestDto updateReq = new WishRequestDto(productId, 5);
        HttpEntity<WishRequestDto> updateEntity = new HttpEntity<>(updateReq, userHeaders);
        ResponseEntity<String> updateRes = restTemplate.exchange(baseUrl + "/wishlist/" + wishId, HttpMethod.PUT, updateEntity, String.class);
        assertThat(updateRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateRes.getBody()).contains("\"quantity\":5");

        // 6. 다른 사용자의 위시리스트 접근(수정/삭제) → 403
        // 관리자 계정으로 위시리스트 삭제 시도
        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.setBearerAuth(adminToken);
        HttpEntity<Void> adminEntity = new HttpEntity<>(adminHeaders);
        ResponseEntity<String> forbiddenRes = restTemplate.exchange(baseUrl + "/wishlist/" + wishId, HttpMethod.DELETE, adminEntity, String.class);
        assertThat(forbiddenRes.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        // 7. 존재하지 않는 상품/위시리스트 접근 → 400
        WishRequestDto badReq = new WishRequestDto(99999L, 1);
        HttpEntity<WishRequestDto> badEntity = new HttpEntity<>(badReq, userHeaders);
        ResponseEntity<String> badRes = restTemplate.exchange(baseUrl + "/wishlist", HttpMethod.POST, badEntity, String.class);
        assertThat(badRes.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // 8. 위시리스트 삭제 → 204
        ResponseEntity<Void> delRes = restTemplate.exchange(baseUrl + "/wishlist/" + wishId, HttpMethod.DELETE, getEntity, Void.class);
        assertThat(delRes.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("위시리스트 시나리오(상품목록-추가-삭제-수정) 흐름 테스트")
    void wishlist_scenario_flow() {
        // 1. 상품 목록 조회 (GET /api/products)
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(userToken);
        HttpEntity<Void> getProductEntity = new HttpEntity<>(userHeaders);
        ResponseEntity<String> productListRes = restTemplate.exchange(baseUrl + "/api/products", HttpMethod.GET, getProductEntity, String.class);
        assertThat(productListRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(productListRes.getBody()).contains("테스트상품1");
        assertThat(productListRes.getBody()).contains("테스트상품5");

        // 2. 위시리스트에 상품 A(2번) 추가 (POST /wishlist)
        WishRequestDto wishReq = new WishRequestDto(2L, 1);
        HttpEntity<WishRequestDto> addEntity = new HttpEntity<>(wishReq, userHeaders);
        ResponseEntity<String> addRes = restTemplate.exchange(baseUrl + "/wishlist", HttpMethod.POST, addEntity, String.class);
        assertThat(addRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(addRes.getBody()).contains("\"product\":{\"id\":2");

        // 3. 위시리스트 목록 조회 (GET /wishlist)
        HttpEntity<Void> getEntity = new HttpEntity<>(userHeaders);
        ResponseEntity<String> getRes = restTemplate.exchange(baseUrl + "/wishlist", HttpMethod.GET, getEntity, String.class);
        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getRes.getBody()).contains("\"product\":{\"id\":2");

        // 4. 위시리스트 수량 변경 (PUT /wishlist/{id})
        String body = getRes.getBody();
        Long wishId = Long.valueOf(body.split("\"id\":")[1].split(",")[0].trim());
        WishRequestDto updateReq = new WishRequestDto(2L, 3);
        HttpEntity<WishRequestDto> updateEntity = new HttpEntity<>(updateReq, userHeaders);
        ResponseEntity<String> updateRes = restTemplate.exchange(baseUrl + "/wishlist/" + wishId, HttpMethod.PUT, updateEntity, String.class);
        assertThat(updateRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateRes.getBody()).contains("\"quantity\":3");

        // 5. 위시리스트 삭제 (DELETE /wishlist/{id})
        ResponseEntity<Void> delRes = restTemplate.exchange(baseUrl + "/wishlist/" + wishId, HttpMethod.DELETE, getEntity, Void.class);
        assertThat(delRes.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // 6. 삭제 후 위시리스트 목록 조회 (GET /wishlist) - 비어있음
        ResponseEntity<String> afterDelRes = restTemplate.exchange(baseUrl + "/wishlist", HttpMethod.GET, getEntity, String.class);
        assertThat(afterDelRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(afterDelRes.getBody()).doesNotContain("\"product\":{\"id\":2");
    }
} 
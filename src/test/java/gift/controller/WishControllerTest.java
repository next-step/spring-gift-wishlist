package gift.controller;

import gift.domain.Product;
import gift.dto.WishResponse;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import gift.service.MemberService;
import gift.service.ProductService;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class WishControllerTest {

    @LocalServerPort
    int port;

    @Autowired MemberService memberService;
    @Autowired ProductService productService;
    @Autowired WishRepository wishRepository;
    @Autowired ProductRepository productRepository;
    @Autowired MemberRepository memberRepository;

    private String baseUrl;
    private RestTemplate restTemplate;

    @PostConstruct
    void setupRestTemplate() {
        this.restTemplate = new RestTemplate();
    }

    @BeforeEach
    void setUpBaseUrl() {
        this.baseUrl = "http://localhost:" + port + "/api/wishes";
    }

    private String createMemberAndGetToken(String email, String password) {
        return memberService.register(email, password);
    }

    private Product createTestProduct(String name, int price) {
        return productService.create(name, price, "http://image.com/image.jpg");
    }

    private HttpHeaders authHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }

    @Test
    @DisplayName("상품을 찜할 수 있다")
    void addWish() {
        String token = createMemberAndGetToken("test@example.com", "abcd@@1234");
        Product product = createTestProduct("테스트상품", 1000);

        HttpHeaders headers = authHeader(token);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(
                Map.of("productId", product.getId()), headers);

        ResponseEntity<WishResponse> response = restTemplate.postForEntity(baseUrl, request, WishResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().productId()).isEqualTo(product.getId());
    }

    @Test
    @DisplayName("같은 상품을 중복 찜하면 409 Conflict가 발생한다")
    void duplicateWish() {
        String token = createMemberAndGetToken("dup@example.com", "abcd@@1234");
        Product product = createTestProduct("중복상품", 500);

        HttpHeaders headers = authHeader(token);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(
                Map.of("productId", product.getId()), headers);

        // 최초 찜 (성공)
        restTemplate.postForEntity(baseUrl, request, WishResponse.class);

        // 중복 찜 시도 시 예외 발생 확인
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.Conflict.class, () -> {
            restTemplate.postForEntity(baseUrl, request, String.class);
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(exception.getResponseBodyAsString()).contains("이미 찜한 상품입니다.");
    }


    @Test
    @DisplayName("찜한 상품을 위시리스트에서 조회할 수 있다")
    void getWishlist() {
        String token = createMemberAndGetToken("view@example.com", "abcd@@1234");
        Product product = createTestProduct("조회상품", 700);

        HttpHeaders headers = authHeader(token);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(
                Map.of("productId", product.getId()), headers);
        restTemplate.postForEntity(baseUrl, request, WishResponse.class);

        HttpEntity<Void> getRequest = new HttpEntity<>(headers);
        ResponseEntity<WishResponse[]> response = restTemplate.exchange(
                baseUrl, HttpMethod.GET, getRequest, WishResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody()[0].productId()).isEqualTo(product.getId());
    }

    @Test
    @DisplayName("찜 항목을 삭제할 수 있다 (멱등성 보장)")
    void removeWish() {
        String token = createMemberAndGetToken("del@example.com", "abcd@@1234");
        Product product = createTestProduct("삭제상품", 900);

        HttpHeaders headers = authHeader(token);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(
                Map.of("productId", product.getId()), headers);
        ResponseEntity<WishResponse> addRes = restTemplate.postForEntity(baseUrl, request, WishResponse.class);

        Long wishId = addRes.getBody().wishId();
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);
        ResponseEntity<Void> deleteRes = restTemplate.exchange(
                baseUrl + "/" + wishId, HttpMethod.DELETE, deleteRequest, Void.class);

        assertThat(deleteRes.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // 이미 삭제된 것을 다시 삭제해도 OK
        ResponseEntity<Void> secondDelete = restTemplate.exchange(
                baseUrl + "/" + wishId, HttpMethod.DELETE, deleteRequest, Void.class);

        assertThat(secondDelete.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("다른 사용자의 찜 항목을 삭제하면 403 Forbidden 발생")
    void removeOthersWish() {
        String token1 = createMemberAndGetToken("me@example.com", "abcd@@1234");
        String token2 = createMemberAndGetToken("other@example.com", "abcd@@1234");
        Product product = createTestProduct("타인상품", 1100);

        // token1으로 찜 등록
        HttpHeaders headers1 = authHeader(token1);
        HttpEntity<Map<String, Object>> req1 = new HttpEntity<>(
                Map.of("productId", product.getId()), headers1);
        ResponseEntity<WishResponse> response = restTemplate.postForEntity(baseUrl, req1, WishResponse.class);
        Long wishId = response.getBody().wishId();

        // token2로 삭제 시도 → 403 예외 발생 기대
        HttpHeaders headers2 = authHeader(token2);
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers2);

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.Forbidden.class, () -> {
            restTemplate.exchange(baseUrl + "/" + wishId, HttpMethod.DELETE, deleteRequest, String.class);
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(exception.getResponseBodyAsString()).contains("다른 사용자의 위시리스트 항목은 삭제할 수 없습니다.");
    }

}

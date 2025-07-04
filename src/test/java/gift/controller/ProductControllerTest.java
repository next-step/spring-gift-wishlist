package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Product;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class ProductControllerTest {

    @LocalServerPort
    private int port;
    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.findAll()
                .forEach(p -> productRepository.deleteById(p.id()));

        baseUrl = "http://localhost:" + port + "/api/products";
    }

    //--- Create Product Tests ---
    @Test
    @DisplayName("POST /api/products - 유효한 상품 생성 성공")
    void createProduct_ValidInput_ReturnsCreated() {
        Product valid = new Product(null, "ValidName", 100, "http://example.com/img.png");

        ResponseEntity<Product> response = restTemplate.postForEntity(baseUrl, valid,
                Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull().extracting(Product::id).isNotNull();
    }

    @Test
    @DisplayName("POST /api/products - 빈 값 입력 시 BAD_REQUEST")
    void createProduct_EmptyNameOrUrl_ReturnsBadRequest() {
        Product invalid = new Product(null, "", 0, "");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, invalid,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("POST /api/products - 이름 길이 초과 시 BAD_REQUEST")
    void createProduct_NameTooLong_ReturnsBadRequest() {
        Product tooLong = new Product(null, "tooLongToMakeItToProductName", 1,
                "http://example.com/img.png");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, tooLong,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("POST /api/products - 허용된 패턴 이름 생성 성공")
    void createProduct_ValidPatternName_ReturnsCreated() {
        Product validPattern = new Product(null, "()[]_유+효-문&자", 2, "http://example.com/img.png");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, validPattern,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("POST /api/products - 패턴 불일치 이름 시 BAD_REQUEST")
    void createProduct_InvalidPatternName_ReturnsBadRequest() {
        Product invalidPattern = new Product(null, "invalid!!!", 2, "http://example.com/img.png");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, invalidPattern,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("POST /api/products - 관리문구 포함 이름 시 BAD_REQUEST")
    void createProduct_ManagementName_ReturnsBadRequest() {
        Product management = new Product(null, "카카오가 들어간 이름", 3, "http://example.com/img.png");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, management,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    //--- Read Product Tests ---
    @Test
    @DisplayName("GET /api/products - 전체 조회 성공")
    void getAllProducts_ReturnsList() {
        productRepository.save(new Product(null, "P1", 10, "u1"));
        productRepository.save(new Product(null, "P2", 20, "u2"));

        ResponseEntity<Product[]> response = restTemplate.getForEntity(baseUrl, Product[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    @DisplayName("GET /api/products/{id} - 존재하지 않는 ID 조회 시 NOT_FOUND")
    void getProductById_NotFound_ReturnsNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    //--- Update Product Tests ---
    @Test
    @DisplayName("PUT /api/products/{id} - 정상 수정 성공")
    void updateProduct_ValidInput_ReturnsOk() {
        Product orig = productRepository.save(new Product(null, "Orig", 50, "u0"));
        Long id = orig.id();

        Product update = new Product(id, "Updated", 75, "u1");
        HttpEntity<Product> entity = new HttpEntity<>(update, createJsonHeaders());

        ResponseEntity<Product> response = restTemplate.exchange(
                baseUrl + "/" + id, HttpMethod.PUT, entity, Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().name()).isEqualTo("Updated");
    }

    @Test
    @DisplayName("PUT /api/products/{id} - 존재하지 않는 ID 수정 시 NOT_FOUND")
    void updateProduct_NotFound_ReturnsNotFound() {
        Product dummy = new Product(999L, "X", 1, "u");
        HttpEntity<Product> entity = new HttpEntity<>(dummy, createJsonHeaders());

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.PUT, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("PUT /api/products - 빈 값 입력 시 BAD_REQUEST")
    void updateProduct_EmptyNameOrUrl_ReturnsBadRequest() {
        Product orig = productRepository.save(new Product(null, "Orig", 50, "u0"));
        Long id = orig.id();
        Product invalid = new Product(null, "", 0, "");
        HttpEntity<Product> entity = new HttpEntity<>(invalid, createJsonHeaders());
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + id, HttpMethod.PUT, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("PUT /api/products/{id} - 패턴 불일치 name 시 BAD_REQUEST")
    void updateProduct_InvalidPatternName_ReturnsBadRequest() {
        Product orig = productRepository.save(new Product(null, "Orig", 50, "u0"));
        Long id = orig.id();
        Product invalidPattern = new Product(id, "invalid!!!", 75, "http://example.com/img.png");
        HttpEntity<Product> entity = new HttpEntity<>(invalidPattern, createJsonHeaders());

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + id, HttpMethod.PUT, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("PUT /api/products/{id} - 이름 길이 초과 시 BAD_REQUEST")
    void updateProduct_NameTooLong_ReturnsBadRequest() {
        Product orig = productRepository.save(new Product(null, "Orig", 50, "u0"));
        Long id = orig.id();
        Product tooLong = new Product(id, "tooLongToMakeItToProductName", 75,
                "http://example.com/img.png");
        HttpEntity<Product> entity = new HttpEntity<>(tooLong, createJsonHeaders());

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + id, HttpMethod.PUT, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("PUT /api/products/{id} - 관리문구 포함 name 시 BAD_REQUEST")
    void updateProduct_ManagementName_ReturnsBadRequest() {
        Product orig = productRepository.save(new Product(null, "Orig", 50, "u0"));
        Long id = orig.id();
        Product management = new Product(id, "카카오가 들어간 이름", 75, "http://example.com/img.png");
        HttpEntity<Product> entity = new HttpEntity<>(management, createJsonHeaders());

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + id, HttpMethod.PUT, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    //--- Delete Product Tests ---
    @Test
    @DisplayName("DELETE /api/products/{id} - 정상 삭제 성공")
    void deleteProduct_ExistingId_ReturnsNoContent() {
        Product dp = productRepository.save(new Product(null, "Del", 30, "uD"));
        Long id = dp.id();

        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + id, HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("DELETE /api/products/{id} - 중복 삭제 시 NOT_FOUND")
    void deleteProduct_NotFound_ReturnsNotFound() {
        Product dp = productRepository.save(new Product(null, "Del", 30, "uD"));
        Long id = dp.id();

        restTemplate.exchange(baseUrl + "/" + id, HttpMethod.DELETE, null, Void.class);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/" + id, HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}

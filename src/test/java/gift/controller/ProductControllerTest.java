package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Product;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.findAllProducts()
            .forEach(p -> productRepository.deleteProduct(p.getId()));
        baseUrl = "http://localhost:" + port + "/admin/products";
    }

    @Test
    void createProduct_success() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("name", "ValidName");
        form.add("price", "1000");
        form.add("imageUrl", "http://image.com");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl,
            new HttpEntity<>(form, formHeaders()), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(productRepository.findAllProducts()).hasSize(1);
    }

    @Test
    void createProduct_validation_fail() {
        // 여러 validation 실패 케이스
        String[][] invalidCases = {
            // name 없음
            {"", "1000", "http://image.com"},
            // 이름에 특수문자 포함
            {"Invalid!!", "1000", "http://image.com"},
            // 이름 길이 초과
            {"ThisProductNameIsWayTooLongForValidation", "1000", "http://image.com"},
            // 금칙어 포함
            {"카카오테크캠퍼스", "1000", "http://image.com"},
            // imageUrl 없음
            {"ValidName", "1000", ""}
        };

        for (String[] c : invalidCases) {
            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("name", c[0]);
            form.add("price", c[1]);
            form.add("imageUrl", c[2]);

            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl,
                new HttpEntity<>(form, formHeaders()), String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    void updateProduct_success_and_not_found() {
        long id = productRepository.createProduct(new Product(null, "Old", 1000, "img"));

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("_method", "put");
        form.add("name", "Updated");
        form.add("price", "2000");
        form.add("imageUrl", "newImg");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/" + id,
            new HttpEntity<>(form, formHeaders()), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(productRepository.findProductById(id).getName()).isEqualTo("Updated");

        // 없는 ID로 요청
        ResponseEntity<String> notFound = restTemplate.postForEntity(baseUrl + "/9999",
            new HttpEntity<>(form, formHeaders()), String.class);
        assertThat(notFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteProduct_success_and_not_found() {
        long id = productRepository.createProduct(new Product(null, "ToDelete", 500, "del"));

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("_method", "delete");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/" + id,
            new HttpEntity<>(form, formHeaders()), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 없는 ID로 요청
        ResponseEntity<String> notFound = restTemplate.postForEntity(baseUrl + "/9999",
            new HttpEntity<>(form, formHeaders()), String.class);
        assertThat(notFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private HttpHeaders formHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }
}

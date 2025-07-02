package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Product;
import gift.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
        List<Product> all = productRepository.findAll();
        all.forEach(p -> productRepository.deleteById(p.id()));

        baseUrl = "http://localhost:" + port + "/api/products";
    }

    @Test
    void createProduct_Success_and_InvalidInput_Fail() {
        Product valid = new Product(null, "ValidName", 100, "http://example.com/img.png");
        ResponseEntity<Product> success = restTemplate.postForEntity(baseUrl, valid, Product.class);
        assertThat(success.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(success.getBody()).isNotNull();
        assertThat(success.getBody().id()).isNotNull();

        Product invalid = new Product(null, "", 0, "");
        ResponseEntity<String> fail = restTemplate.postForEntity(baseUrl, invalid, String.class);
        assertThat(fail.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        Product tooLongName = new Product(null, "tooLongToMakeItToProductName", 1, "http://example.com/img.png");
        ResponseEntity<String> fail2 = restTemplate.postForEntity(baseUrl, tooLongName, String.class);
        assertThat(fail2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        Product validPatternName = new Product(null, "()[]_유+효-문&자", 2, "http://example.com/img.png");
        ResponseEntity<String> suc3 = restTemplate.postForEntity(baseUrl, validPatternName, String.class);
        assertThat(suc3.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Product invalidPatternName = new Product(null, "invalid!!!", 2,  "http://example.com/img.png");
        ResponseEntity<String> fail3 = restTemplate.postForEntity(baseUrl, invalidPatternName, String.class);
        assertThat(fail3.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getAllProducts_and_GetById_NotFound() {
        Product p1 = productRepository.save(new Product(null, "P1", 10, "u1"));
        Product p2 = productRepository.save(new Product(null, "P2", 20, "u2"));

        ResponseEntity<Product[]> all = restTemplate.getForEntity(baseUrl, Product[].class);
        assertThat(all.getStatusCode()).isEqualTo(HttpStatus.OK);
        Product[] list = all.getBody();
        assertThat(list).hasSize(2);

        ResponseEntity<String> notFound = restTemplate.getForEntity(baseUrl + "/999", String.class);
        assertThat(notFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateProduct_Success_and_NotFound() {
        Product orig = productRepository.save(new Product(null, "Orig", 50, "u0"));
        Long id = orig.id();

        Product update = new Product(id, "Updated", 75, "u1");
        HttpEntity<Product> entity = new HttpEntity<>(update, createJsonHeaders());
        ResponseEntity<Product> updated = restTemplate.exchange(
                baseUrl + "/" + id, HttpMethod.PUT, entity, Product.class);
        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updated.getBody().name()).isEqualTo("Updated");

        Product dummy = new Product(999L, "X", 1, "u");
        HttpEntity<Product> badEntity = new HttpEntity<>(dummy, createJsonHeaders());
        ResponseEntity<String> notFound = restTemplate.exchange(
                baseUrl + "/999", HttpMethod.PUT, badEntity, String.class);
        assertThat(notFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteProduct_Success_and_NotFound() {
        Product dp = productRepository.save(new Product(null, "Del", 30, "uD"));
        Long id = dp.id();

        ResponseEntity<Void> removed = restTemplate.exchange(
                baseUrl + "/" + id, HttpMethod.DELETE, null, Void.class);
        assertThat(removed.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> notFound = restTemplate.exchange(
                baseUrl + "/" + id, HttpMethod.DELETE, null, String.class);
        assertThat(notFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}

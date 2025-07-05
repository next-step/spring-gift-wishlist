package gift;

import static org.assertj.core.api.Assertions.assertThat;

import gift.dto.ProductCreateResponse;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("일반 사용자용 Product API 테스트")
public class ProductApiTest {

    private final RestClient restClient = RestClient.builder().build();

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM products");
        jdbcTemplate.update("ALTER TABLE products ALTER COLUMN id RESTART WITH 1");

        jdbcTemplate.update("INSERT INTO products (name, price, image_url) VALUES (?, ?, ?)", "p1",
            1000, "url1");
        jdbcTemplate.update("INSERT INTO products (name, price, image_url) VALUES (?, ?, ?)", "p2",
            2000, "url2");
        jdbcTemplate.update("INSERT INTO products (name, price, image_url) VALUES (?, ?, ?)", "p3",
            3000, "url3");
        jdbcTemplate.update("INSERT INTO products (name, price, image_url) VALUES (?, ?, ?)", "p4",
            4000, "url4");
        jdbcTemplate.update("INSERT INTO products (name, price, image_url) VALUES (?, ?, ?)", "p5",
            5000, "url5");
    }

    @Test
    @DisplayName("상품 단건 조회")
    void 상품_조회() {
        var url = "http://localhost:" + port + "/api/products/1";

        var response = restClient.get()
            .uri(url)
            .retrieve()
            .toEntity(ProductResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("p1");
    }

    @Test
    @DisplayName("상품 전체 조회")
    void 상품_전체_조회() {
        var url = "http://localhost:" + port + "/api/products/all";

        var response = restClient.get()
            .uri(url)
            .retrieve()
            // List로 변환하지 않고 배열로 간단하게 처리
            .toEntity(ProductResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .isNotNull()
            .hasSize(5);
    }

    @Test
    @DisplayName("상품 등록")
    void 상품_등록() {
        var url = "http://localhost:" + port + "/api/products";
        ProductRequest request = new ProductRequest(null, "test1", 100, "test url");

        var response = restClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .toEntity(ProductCreateResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().id()).isNotNull();
    }

    @Test
    @DisplayName("상품 수정")
    void 상품_수정() {
        Long productId = 5L;
        var url = "http://localhost:" + port + "/api/products/" + productId;
        ProductRequest updateRequest = new ProductRequest(productId, "updated name", 2000,
            "updated url");

        var response = restClient.patch()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(updateRequest)
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        var getResponse = restClient.get()
            .uri(url)
            .retrieve()
            .toEntity(ProductResponse.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().name()).isEqualTo("updated name");
        assertThat(getResponse.getBody().price()).isEqualTo(2000);
    }


    @Test
    @DisplayName("상품 삭제")
    void 상품_삭제() {
        Long productId = 5L;
        var url = "http://localhost:" + port + "/api/products/" + productId;

        var response = restClient.delete()
            .uri(url)
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        var notFoundResponse = restClient.get()
            .uri(url)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(), (req, res) -> {
            })
            .toBodilessEntity();

        assertThat(notFoundResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

//    @Test
//    @DisplayName("상품 단건 조회 예외발생")
//
//    @Test
//    @DisplayName("상품 둥록 예외 발생")
//
//    @Test
//    @DisplayName("상품 수정 예외 발생")
//
//    @Test
//    @DisplayName("상품 삭제 예외 발생")


}

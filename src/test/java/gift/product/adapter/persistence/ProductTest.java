package gift.product.adapter.persistence;

import gift.product.application.port.in.dto.ProductRequest;
import gift.product.application.port.in.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class ProductTest {

    private static final String NAME = "프로덕트";
    private static final int PRICE = 100;
    private static final String IMAGE_URL = "https://test.com/img.jpg";
    @LocalServerPort
    private int port;
    private String baseUrl;
    private RestClient restClient;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Test
    @DisplayName("유효한 상품 정보를 등록하면 CREATED 상태코드와 Location 헤더를 반환한다")
    void CREATE() {
        // given
        ProductRequest productRequest = ProductRequest.of(NAME, PRICE, IMAGE_URL);

        // when
        ResponseEntity<Void> response = restClient.post()
                .uri("/api/products")
                .body(productRequest)
                .retrieve()
                .toBodilessEntity();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getHeaders().getLocation()).toString()).contains("/api/products");
    }

    @Test
    @DisplayName("ID로 상품을 조회하면 상품 정보를 반환한다")
    void READ() {
        // given
        URI location = createProduct(NAME, PRICE, IMAGE_URL);

        // when
        ProductResponse response = restClient.get()
                .uri(location)
                .retrieve()
                .body(ProductResponse.class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo(NAME);
        assertThat(response.price()).isEqualTo(PRICE);
    }

    @Test
    @DisplayName("존재하는 상품을 수정하면 NO_CONTENT 상태코드를 반환하고 정보가 변경된다")
    void UPDATE() {
        // given
        URI location = createProduct(NAME, PRICE, IMAGE_URL);
        ProductRequest updateRequest = ProductRequest.of("수정된 프로덕트", 2000, "https://new.img.url");

        // when
        ResponseEntity<Void> response = restClient.put()
                .uri(location)
                .body(updateRequest)
                .retrieve()
                .toBodilessEntity();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ProductResponse updatedProduct = restClient.get().uri(location).retrieve().body(ProductResponse.class);
        assertThat(updatedProduct.name()).isEqualTo("수정된 프로덕트");
        assertThat(updatedProduct.price()).isEqualTo(2000);
    }

    @Test
    @DisplayName("존재하는 상품을 삭제하면 NO_CONTENT 상태코드를 반환하고 조회가 불가능하다")
    void DELETE() {
        // given
        URI location = createProduct(NAME, PRICE, IMAGE_URL);

        // when
        ResponseEntity<Void> response = restClient.delete()
                .uri(location)
                .retrieve()
                .toBodilessEntity();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThatThrownBy(() ->
                restClient.get()
                        .uri(location)
                        .retrieve()
                        .toBodilessEntity())
                .isInstanceOf(HttpClientErrorException.NotFound.class);
    }

    private URI createProduct(String name, int price, String imageUrl) {
        ProductRequest productRequest = ProductRequest.of(name, price, imageUrl);
        ResponseEntity<Void> response = restClient.post()
                .uri("/api/products")
                .body(productRequest)
                .retrieve()
                .toBodilessEntity();
        return response.getHeaders().getLocation();
    }
}

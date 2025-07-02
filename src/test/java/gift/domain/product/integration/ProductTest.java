package gift.domain.product.integration;


import gift.domain.product.dto.ProductRequest;
import static org.assertj.core.api.Assertions.*;

import gift.domain.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductTest {

    @LocalServerPort
    private int port;
    private String baseUrl;
    private RestClient restClient;
    private List<ProductRequest> productRequests = new ArrayList<>();

    private static final String NAME = "프로덕트";
    private static final String IMAGE_URL = "https://test.com/img.jpg";

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;

        restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();

        for (int i = 1000; i <= 5000; i+=1000) {
            productRequests.add(new ProductRequest(NAME, i, IMAGE_URL));
        }
    }

    @Test
    void 시나리오1_하나의_상품_추가하고_조회하고_수정하고_삭제하기() {

        //CREATE
        ResponseEntity<Void> createResponse = restClient.post()
                .uri("/api/products")
                .body(productRequests.getFirst())
                .retrieve()
                .toBodilessEntity();
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getHeaders().getLocation().toString()).contains("/api/products/");

        URI location = createResponse.getHeaders().getLocation();
        assertThat(location.getPath()).isEqualTo("/api/products/1");

        //READ
        ProductResponse response = restClient.get()
                .uri("api/products/{id}", 1)
                .retrieve()
                .body(ProductResponse.class);
        assertThat(response.getName()).isEqualTo(NAME);
        assertThat(response.getPrice()).isEqualTo(1000);
        assertThat(response.getImageUrl()).isEqualTo(IMAGE_URL);
        //UPDATE
        ProductRequest updatedRequest = new ProductRequest("수정본",2000,"https://test.com/img2.jpg");
        ResponseEntity<Void> entity = restClient.put()
                .uri("api/products/{id}", 1)
                .body(updatedRequest)
                .retrieve()
                .toBodilessEntity();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ProductResponse response2 = restClient.get()
                .uri("api/products/{id}", 1)
                .retrieve()
                .body(ProductResponse.class);

        assertThat(response2.getName()).isEqualTo("수정본");
        assertThat(response2.getPrice()).isEqualTo(2000);
        assertThat(response2.getImageUrl()).isEqualTo("https://test.com/img2.jpg");

        //DELETE
        ResponseEntity<Void> bodilessEntity = restClient.delete()
                .uri("api/products/{id}", 1)
                .retrieve()
                .toBodilessEntity();
        assertThat(bodilessEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThatThrownBy(()->
                restClient.get()
                        .uri("/api/products/{id}", 1)
                        .retrieve()
                        .body(ProductResponse.class)).isInstanceOf(HttpClientErrorException.NotFound.class);
    }
    @Test
    void 시나리오2_여러개의_상품_추가하고_하나_조회하고_수정하고_조회하기() {

    }

}

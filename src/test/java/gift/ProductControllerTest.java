package gift;


import gift.dto.request.ProductRequestDto;
import gift.dto.response.ProductResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private RestClient.Builder builder;

    private RestClient client;

    @BeforeEach
    void setUp() {
        this.client = builder.baseUrl("http://localhost:" + port).build();
    }

    @Test
    void 이름에_카카오가_포함되지_않은_상품_성공적으로_추가() {
        var newProduct = new ProductRequestDto(
                "새로운 상품",
                10000L,
                "http://image.url",
                false);

        var response = client.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(newProduct)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().name()).isEqualTo("새로운 상품");
        assertThat(response.getBody().imageUrl()).isEqualTo("http://image.url");
        assertThat(response.getBody().price()).isEqualTo(10000L);
    }

    @Test
    void 이름에_카카오가_포함된_상품_성공적으로_추가(){
        var newProduct = new ProductRequestDto(
                "카카오 상품",
                10000L,
                "http://image.url",
                true
        );

        var response = client.post()
                .uri("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(newProduct)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().name()).isEqualTo("카카오 상품");
        assertThat(response.getBody().imageUrl()).isEqualTo("http://image.url");
        assertThat(response.getBody().price()).isEqualTo(10000L);
    }

    @Test
    void 전체상품목록_성공적으로_조회(){
        var response = client.get()
                .uri("/api/products")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ProductResponseDto>>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void 상품아이디로_단일상품_성공적으로_조회(){
        var productId = 3L;

        var response = client.get()
                .uri("/api/products/" + productId)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().name()).isEqualTo("립스틱");
        assertThat(response.getBody().imageUrl()).isEqualTo("https://i.namu.wiki/i/0GQWD4DtVb4MCjTzYV3lvvFjQE2J05KIoU2-5TPX1Zvq3TXum5D1vYcBkVJndm0YrUntKAwNLRw-qgJ2gtD_Tic8zHCnbTEX5-OEUoiVQ_p_-hS9uD0S8zDak7jcwNtvPvqqjhjRT_NbvPMGt-6HaA.webp");
        assertThat(response.getBody().price()).isEqualTo(50000L);
        assertThat(response.getBody().id()).isEqualTo(productId);
    }

    @Test
    void 상품정보_성공적으로_수정(){
        var productId = 3L;
        var updateProduct = new ProductRequestDto("수정된 상품", 10000L,  "http://image.url",  false);

        var response = client.put()
                .uri("/api/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateProduct)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().name()).isEqualTo("수정된 상품");
        assertThat(response.getBody().imageUrl()).isEqualTo("http://image.url");
        assertThat(response.getBody().price()).isEqualTo(10000L);
        assertThat(response.getBody().id()).isEqualTo(productId);
    }

    @Test
    void 상품정보_성공적으로_수정_이름에_카카오포함(){
        var productId = 3L;
        var updateProduct = new ProductRequestDto("카카오 상품", 10000L,  "http://image.url",  true);

        var response = client.put()
                .uri("/api/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateProduct)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().name()).isEqualTo("카카오 상품");
        assertThat(response.getBody().imageUrl()).isEqualTo("http://image.url");
        assertThat(response.getBody().price()).isEqualTo(10000L);
        assertThat(response.getBody().id()).isEqualTo(productId);
    }

    @Test
    void 상품_성공적으로_삭제(){
        var productId = 3L;

        var response = client.delete()
                .uri("/api/products/" + productId)
                .retrieve()
                .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        var updateResponse = client.get()
                .uri("/api/products/" + productId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,(req, res) -> {})
                .toEntity(String.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}

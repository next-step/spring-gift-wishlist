package gift;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductE2ETest {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void 상품을_등록하고_조회() {
        // 상품 등록
        ProductRequestDto request = new ProductRequestDto();
        request.setName("녹차");
        request.setPrice(3500);
        request.setImageUrl("green_tea.jpg");

        restClient.post()
                .uri("/api/products")
                .body(request)
                .retrieve()
                .toBodilessEntity();

        // 상품 목록 조회
        ProductResponseDto[] response = restClient.get()
                .uri("/api/products")
                .retrieve()
                .body(ProductResponseDto[].class);

        assertThat(response).isNotEmpty();
    }

    @Test
    void 상품을_수정하고_조회(){
        // 상품 수정
        ProductRequestDto request = new ProductRequestDto();
        request.setName("아이스 아메리카노");
        request.setPrice(5000);
        request.setImageUrl("ice_americano.jpg");

        restClient.put()
                .uri("/api/products/1")
                .body(request)
                .retrieve()
                .toBodilessEntity();

        // 상품 단건 조회
        ProductResponseDto response = restClient.get()
                .uri("/api/products/1")
                .retrieve()
                .toEntity(ProductResponseDto.class)
                .getBody();

        Assertions.assertNotNull(response);
        assertThat(response.price()).isEqualTo(5000);
    }

    @Test
    void 상품을_삭제하고_조회(){
        // 상품 삭제
        restClient.delete()
                .uri("/api/products/1")
                .retrieve()
                .toBodilessEntity();

        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () -> restClient.get()
                                .uri("/api/products/1")
                                .retrieve()
                                .toEntity(Void.class)
                );

    }
}
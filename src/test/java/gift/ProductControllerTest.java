package gift;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.UpdateProductRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {
    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();
    private String url;

    @BeforeEach
    void setUp() {
        url = "http://localhost:" + port + "/api/products";
    }

    @Test
    void 올바른_상품_생성() {
        ProductRequestDto productRequestDto = new ProductRequestDto(
                "치킨",
                10000,
                "https://picsum.photos/200",
                false
        );

        var response = client.post()
                .uri(url)
                .body(productRequestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("치킨");
    }

    @Test
    void MD승인_없이_카카오_이름이_들어간_상품_생성() {
        ProductRequestDto productRequestDto = new ProductRequestDto(
                "카카오치킨",
                10000,
                "https://picsum.photos/200",
                false
        );

        assertThatThrownBy(() ->
                client.post()
                        .uri(url)
                        .body(productRequestDto)
                        .retrieve()
                        .toBodilessEntity()
        ).isInstanceOf(HttpClientErrorException.BadRequest.class);
    }

    @Test
    void MD승인_받고_카카오_이름이_들어간_상품_생성() {
        ProductRequestDto productRequestDto = new ProductRequestDto(
                "카카오치킨",
                10000,
                "https://picsum.photos/200",
                true
        );

        var response = client.post()
                .uri(url)
                .body(productRequestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("카카오치킨");
    }

    @Test
    void MD승인_없이_카카오_이름으로_상품_수정() {
        ProductRequestDto productRequestDto = new ProductRequestDto(
                "치킨",
                10000,
                "https://picsum.photos/200",
                false
        );

        var response = client.post()
                .uri(url)
                .body(productRequestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getBody()).isNotNull();
        Long productId = response.getBody().id();

        UpdateProductRequestDto updateProductRequestDto = new UpdateProductRequestDto(
                productId,
                "카카오치킨",
                10000,
                "https://picsum.photos/200",
                false
        );

        assertThatThrownBy(() ->
                client.put()
                        .uri(url + "/" + productId)
                        .body(updateProductRequestDto)
                        .retrieve()
                        .toBodilessEntity()
        ).isInstanceOf(HttpClientErrorException.BadRequest.class);
    }

}

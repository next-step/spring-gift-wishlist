package gift;

import gift.dto.ProductPatchDto;
import gift.dto.ProductRequestDto;
import gift.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    private final String baseUrl = "http://localhost:";

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create();
    }

    @Test
    void 유효한_생성_시_201_CREATED() {
        String url = baseUrl + port + "/api/products";
        ProductRequestDto requestDto = new ProductRequestDto(
                "아이스 카페 아메리카노 T",
                4700,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
        );
        ResponseEntity<Product> response = restClient.post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void 유효하지_않은_생성_시_400_BAD_REQUEST() {
        String url = baseUrl + port + "/api/products";
        ProductRequestDto requestDto = new ProductRequestDto(null, null, null);
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                restClient.post()
                                        .uri(url)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(Void.class)
                );
    }

    @Test
    void 유효한_조회_시_200_OK() {
        String url = baseUrl + port + "/api/products/1";
        ResponseEntity<Product> response = restClient.get()
                .uri(url)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void 유효하지_않은_조회_시_404_NOT_FOUND() {
        String url = baseUrl + port + "/api/products/0";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () ->
                                restClient.get()
                                        .uri(url)
                                        .retrieve()
                                        .toEntity(Void.class)
                );
    }

    @Test
    void 유효한_수정_시_200_OK() {
        String url = baseUrl + port + "/api/products/1";
        ProductPatchDto patchDto = new ProductPatchDto(
                "각하오 커피",
                7800,
                null
        );
        ResponseEntity<Product> response = restClient.patch()
                .uri(url)
                .body(patchDto)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void 유효하지_않은_수정_시_400_BAD_REQUEST() {
        String url = baseUrl + port + "/api/products/1";
        ProductPatchDto patchDto = new ProductPatchDto(null, null, null);
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                restClient.patch()
                                        .uri(url)
                                        .body(patchDto)
                                        .retrieve()
                                        .toEntity(Product.class)
                );
    }

    @Test
    void 리스트_조회_시_200_OK() {
        String url = baseUrl + port + "/api/products";
        ResponseEntity<List<Product>> response = restClient.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<Product>>() {});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isNotZero();
    }

    @Test
    void 유효한_삭제_시_204_NO_CONTENT() {
        String url = baseUrl + port + "/api/products/1";
        ResponseEntity<Void> response = restClient.delete()
                .uri(url)
                .retrieve()
                .toEntity(Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void 유효하지_않은_삭제_시_404_NOT_FOUND() {
        String url = baseUrl + port + "/api/products/0";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () ->
                                restClient.delete()
                                        .uri(url)
                                        .retrieve()
                                        .toEntity(Void.class)
                );
    }


}

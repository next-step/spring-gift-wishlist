package gift.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductRequestDtoTest {
    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();
    private String url;

    @BeforeEach
    void setUp() {
        url = "http://localhost:" + port + "/api/products";
    }

    @Test
    @DisplayName("Product 생성 시, 검증된 input을 넣으면 201(Created)이 반환된다. ")
    void createProduct_withValidInput_returns201() {
        ProductRequestDto productRequestDto = new ProductRequestDto(
                "하리보 젤리(콜라맛)",
                2000,
                "http://img.url/test.png"
        );

        var response = client.post()
                .uri(url)
                .body(productRequestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ProductResponseDto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.name()).isEqualTo("하리보 젤리(콜라맛)");
        assertThat(body.price()).isEqualTo(2000);
        assertThat(body.imageUrl()).isEqualTo("http://img.url/test.png");
    }

    @Test
    @DisplayName("Product 생성 시, 상품명이 15자를 초과한 경우 400(Bad Request)를 반환한다. ")
    void createProduct_withNameTooLong_returns400() {
        String name = "하리보맛있네또사먹을게요많이파세요"; // 총 17자
        ProductRequestDto productRequestDto = new ProductRequestDto(name, 50, null);

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        client.post()
                                .uri(url)
                                .body(productRequestDto)
                                .retrieve()
                                .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String msg = ex.getResponseBodyAsString();
                    assertThat(msg).contains("상품명은 최대 15자까지 입력할 수 있습니다.");
                });
    }

    @Test
    @DisplayName("Product 생성 시, 허용되지 않은 특수문자를 포함한 경우 400(Bad Request)를 반환한다. ")
    void createProduct_withBadCharacters_returns400() {
        ProductRequestDto productRequestDto = new ProductRequestDto("하리보!@", 20, null);

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        client.post()
                                .uri(url)
                                .body(productRequestDto)
                                .retrieve()
                                .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String msg = ex.getResponseBodyAsString();
                    assertThat(msg).contains("상품 이름에 허용되지 않는 문자가 포함되어 있습니다.");
                });
    }

    @Test
    @DisplayName("Product 생성 시, 음수 가격을 입력한 경우 400(Bad Request)를 반환한다. ")
    void createProduct_withNegativePrice_returns400() {
        ProductRequestDto productRequestDto = new ProductRequestDto("하리보 젤리", -2000, null);

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        client.post()
                                .uri(url)
                                .body(productRequestDto)
                                .retrieve()
                                .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String msg = ex.getResponseBodyAsString();
                    assertThat(msg).contains("가격은 0 이상이어야 합니다.");
                });
    }

    @Test
    @DisplayName("Product 생성 시, '카카오'가 포함된 문구를 입력한 경우 400(Bad Request)를 반환한다. ")
    void createProduct_withKakaoInName_returns400() {
        ProductRequestDto productRequestDto = new ProductRequestDto("카카오 젤리", 3000, null);

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        client.post()
                                .uri(url)
                                .body(productRequestDto)
                                .retrieve()
                                .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String msg = ex.getResponseBodyAsString();
                    assertThat(msg).contains("'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
                });
    }
}

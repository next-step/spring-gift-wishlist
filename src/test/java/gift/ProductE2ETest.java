package gift;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        ProductRequestDto request = new ProductRequestDto(null, "녹차", 3500, "green_tea.jpg");

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
        ProductRequestDto request = new ProductRequestDto(null, "아이스 아메리카노", 5000, "ice_america.jpg");

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

        // try-catch로 예외 처리
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            restClient.get()
                    .uri("/api/products/1")
                    .retrieve()
                    .toBodilessEntity();
        });
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void 상품_등록_유효성_검사_실패() {
        ProductRequestDto invalidRequest = new ProductRequestDto(null, "@카카오@", 10, "kakao.jpg");

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            restClient.post()
                    .uri("/api/products")
                    .body(invalidRequest)
                    .retrieve()
                    .toBodilessEntity();
        });

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        String responseBody = exception.getResponseBodyAsString();

        assertThat(responseBody).contains("특수문자는 ()[]+-&/_ 만 사용할 수 있어요.");
        assertThat(responseBody).contains("상품명에 <카카오>가 포함된 상품은 담당 MD에게 문의해주세요.");
        assertThat(responseBody).contains("가격은 100원 이상으로 등록해주세요.");
    }
}
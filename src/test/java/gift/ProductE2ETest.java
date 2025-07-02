package gift;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import gift.api.dto.ProductRequestDto;
import gift.api.dto.ProductResponseDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductE2ETest {

    @LocalServerPort
    private int port;

    RestClient restclient;

    @BeforeEach
    void setUp() {
        restclient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void 상품_등록_테스트() {
        ProductRequestDto request = new ProductRequestDto("Product 1", 100L,
                "https://image.com/1.jpg");

        ProductResponseDto response = restclient.post()
                .uri("/api/products")
                .body(request)
                .retrieve()
                .body(ProductResponseDto.class);

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals("Product 1", response.name());
        assertEquals(100L, response.price());
        assertEquals("https://image.com/1.jpg", response.imageUrl());
    }

    @Test
    void 상품_단건_조회_테스트() {
        ProductRequestDto request = new ProductRequestDto("Product 2", 500L,
                "https://image.com/2.jpg");
        ProductResponseDto created = restclient.post()
                .uri("/api/products")
                .body(request)
                .retrieve()
                .body(ProductResponseDto.class);

        ProductResponseDto found = restclient.get()
                .uri("/api/products/" + created.id())
                .retrieve()
                .body(ProductResponseDto.class);

        assertEquals(created.id(), found.id());
        assertEquals("Product 2", found.name());
    }

    @Test
    void 상품_전체_조회_테스트() {
        List<?> productList = restclient.get()
                .uri("/api/products")
                .retrieve()
                .body(List.class);

        assertNotNull(productList);
        assertTrue(productList.size() >= 0);
    }

    @Test
    void 상품_수정_테스트() {
        ProductRequestDto request = new ProductRequestDto("Product 3", 200L,
                "https://image.com/3.jpg");
        ProductResponseDto created = restclient.post()
                .uri("/api/products")
                .body(request)
                .retrieve()
                .body(ProductResponseDto.class);

        ProductRequestDto updated = new ProductRequestDto("UpdatedProd3", 999L,
                "https://updated.com/3.jpg");
        ProductResponseDto result = restclient.put()
                .uri("/api/products/" + created.id())
                .body(updated)
                .retrieve()
                .body(ProductResponseDto.class);

        assertEquals("UpdatedProd3", result.name());
        assertEquals(999L, result.price());
    }

    @Test
    void 상품_삭제_테스트() {
        ProductRequestDto request = new ProductRequestDto("Product 4", 300L,
                "https://image.com/4.jpg");
        ProductResponseDto created = restclient.post()
                .uri("/api/products")
                .body(request)
                .retrieve()
                .body(ProductResponseDto.class);

        restclient.delete()
                .uri("/api/products/" + created.id())
                .retrieve()
                .toBodilessEntity();

        try {
            restclient.get()
                    .uri("/api/products/" + created.id())
                    .retrieve()
                    .body(ProductResponseDto.class);
            fail("삭제된 상품이 조회되면 안 됩니다");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void 금지_단어_검증_실패_테스트() {
        ProductRequestDto request = new ProductRequestDto("카카오커피", 100L,
                "https://image.com/bad.jpg");
        try {
            restclient.post()
                    .uri("/api/products")
                    .body(request)
                    .retrieve()
                    .body(ProductResponseDto.class);
            fail("검증 오류가 발생해야 합니다");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertTrue(e.getResponseBodyAsString().contains("담당 MD의 승인이 필요한 단어"));
        }
    }
}
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
    void 상품_전체_조회_테스트() {
        List<?> productList = restclient.get()
                .uri("/api/products")
                .retrieve()
                .body(List.class);

        assertNotNull(productList);
        assertTrue(productList.size() >= 0);
    }

    @Test
    void 특정_상품_조회_테스트() {
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
    void 상품_추가_테스트() {
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
    void 상품_이름_최대_15자_실패() {
        ProductRequestDto request = new ProductRequestDto("이름이너무길어서검증에걸리는상품", 100L,
                "https://image.com/longname.jpg");

        try {
            restclient.post()
                    .uri("/api/products")
                    .body(request)
                    .retrieve()
                    .body(ProductResponseDto.class);
            fail("검증 오류가 발생해야 합니다");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertTrue(e.getResponseBodyAsString().contains("상품 이름은 최대 15자여야 합니다."));
        }
    }

    @Test
    void 상품_이름_특수_문자_성공() {
        ProductRequestDto request = new ProductRequestDto("()[]+-&/_ 이건 됨", 100L,
                "https://image.com/success.jpg");

        ProductResponseDto response = restclient.post()
                .uri("/api/products")
                .body(request)
                .retrieve()
                .body(ProductResponseDto.class);

        assertNotNull(response);
        assertEquals("()[]+-&/_ 이건 됨", response.name());
        assertEquals(100L, response.price());
        assertEquals("https://image.com/success.jpg", response.imageUrl());
    }

    @Test
    void 상품_이름_특수_문자_실패() {
        ProductRequestDto request = new ProductRequestDto("!이건 안됨!", 100L,
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
            assertTrue(e.getResponseBodyAsString()
                    .contains("상품 이름에는 (), [], +, -, &, /, _ 외의 특수 문자를 사용할 수 없습니다."));
        }
    }

    @Test
    void 상품_이름_MD_승인_글자() {
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

    @Test
    void 상품_가격_0원_이상_실패() {
        ProductRequestDto request = new ProductRequestDto("Invalid Price", -100L,
                "https://image.com/invalid.jpg");

        try {
            restclient.post()
                    .uri("/api/products")
                    .body(request)
                    .retrieve()
                    .body(ProductResponseDto.class);
            fail("검증 오류가 발생해야 합니다");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertTrue(e.getResponseBodyAsString().contains("가격은 0 이상이어야 합니다."));
        }
    }
}
package gift.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.dto.api.AddProductRequestDto;
import gift.dto.api.ProductResponseDto;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {
    
    @LocalServerPort
    private int port;
    
    private RestClient restClient;
    
    @BeforeEach
    void setUp() {
        this.restClient = RestClient.builder()
            .baseUrl("http://localhost:" + port)
            .build();
    }
    
    @Test
    void 정상적으로_상품을_등록한다() {
        var request = new AddProductRequestDto("테스트 상품", 1000L, "https://test.com/image.jpg");
        
        var response = restClient.post()
            .uri("/api/products")
            .body(request)
            .retrieve()
            .body(ProductResponseDto.class);
        
        assertThat(response.getName()).isEqualTo("테스트 상품");
    }
    
    @Test
    void 옳지_않은_상품명_추가를_시도한다() throws IOException {
        var request = new AddProductRequestDto("테스트:상품", 1000L, "https://test.com/image.jpg");
        
        var response = restClient.post()
            .uri("/api/products")
            .body(request)
            .exchange((req, res) -> res);
        
        assertThat(response.getStatusCode().value()).isEqualTo(400); // HTTP 400 확인
    }
    
    @Test
    void 너무_긴_상품명_추가를_시도한다() throws IOException {
        var request = new AddProductRequestDto("15자를넘겨야하는데뭐라고할까고민좀했음", 1000L, "https://test.com/image.jpg");
        
        var response = restClient.post()
            .uri("/api/products")
            .body(request)
            .exchange((req, res) -> res);
        
        assertThat(response.getStatusCode().value()).isEqualTo(400); // HTTP 400 확인
    }
    
    @Test
    void 상품_검색을_시도한다() {
        
        var response = restClient.get()
            .uri("/api/products/1")
            .retrieve()
            .body(ProductResponseDto.class);
        
        assertThat(response.getName()).isEqualTo("아메리카노");
    }
    
    @Test
    void 전체_상품_검색을_시도한다() {
        
        var response = restClient.get()
            .uri("/api/products")
            .retrieve()
            .body(ProductResponseDto[].class);
        
        assertThat(response[0].getName()).isEqualTo("아메리카노");
    }
}
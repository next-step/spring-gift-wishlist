package gift.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class E2ETest {
    @LocalServerPort
    private int port;

    private String baseUrl;

    RestClient restClient;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/products";
        restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    @Test
    @DisplayName("상품 목록 조회 테스트")
    void 상품_목록_조회_테스트() {
        var response = restClient.get()
            .retrieve()
            .toEntity(new ParameterizedTypeReference<List<ProductResponseDto>>() {
            });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getFirst().name()).isEqualTo("샘플 상품1");
    }

    @Test
    @DisplayName("단일 상품 조회 테스트")
    void 단일_상품_조회_테스트() {
        var response = restClient.get()
            .uri("/1")
            .retrieve()
            .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().name()).isEqualTo("샘플 상품1");
    }

    @Test
    @DisplayName("존재하지 않는 상품 조회 테스트")
    void 존재하지_않는_상품_테스트() {
        var exception = assertThrows(HttpClientErrorException.class, () ->
            restClient.get()
                .uri("/99")
                .retrieve()
                .toEntity(ProductResponseDto.class));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("상품 생성 테스트")
    void 상품_생성_테스트() {
        ProductRequestDto requestDto = new ProductRequestDto("샘플 상품3", 30000, "sample3.jpg");
        var response = restClient.post()
            .body(requestDto)
            .retrieve()
            .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().name()).isEqualTo("샘플 상품3");
    }

    @Test
    @DisplayName("이름이 15자 초과인 상품 생성 테스트")
    void 이름이_15자_초과인_상품_생성_테스트() {
        ProductRequestDto requestDto = new ProductRequestDto("이름이 15자를 초과해서 오류가 발생하는 샘플 상품", 10000, "sample.jpg");
        var exception = assertThrows(HttpClientErrorException.class, () ->
            restClient.post()
                .body(requestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("이름에 사용 불가 특수문자가 포함된 상품 생성 테스트")
    void 이름에_사용_불가_특수문자가_포함된_상품_생성_테스트() {
        ProductRequestDto requestDto = new ProductRequestDto("상품!", 10000, "sample.jpg");
        var exception = assertThrows(HttpClientErrorException.class, () ->
            restClient.post()
                .body(requestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("이름에 카카오가 포함된 상품 생성 테스트")
    void 이름에_카카오가_포함된_상품_생성_테스트() {
        ProductRequestDto requestDto = new ProductRequestDto("카카오 상품", 10000, "sample.jpg");
        var exception = assertThrows(HttpClientErrorException.class, () ->
            restClient.post()
                .body(requestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("상품 수정 테스트")
    void 상품_수정_테스트() {
        ProductRequestDto requestDto = new ProductRequestDto("수정된 샘플 상품1", 100000, "updated1.jpg");
        var response = restClient.put()
            .uri("/1")
            .body(requestDto)
            .retrieve()
            .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().name()).isEqualTo("수정된 샘플 상품1");
    }

    @Test
    @DisplayName("존재하지 않는 상품 수정 테스트")
    void 존재하지_않는_상품_수정_테스트() {
        ProductRequestDto requestDto = new ProductRequestDto("수정된 샘플 상품99", 990000, "updated99.jpg");
        var exception = assertThrows(HttpClientErrorException.class, () ->
            restClient.put()
                .uri("/99")
                .body(requestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void 상품_삭제_테스트() {
        var response = restClient.delete()
            .uri("/1")
            .retrieve()
            .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("존재하지 않는 상품 삭제 테스트")
    void 존재하지_않는_상품_삭제_테스트() {
        var exception = assertThrows(HttpClientErrorException.class, () ->
            restClient.delete()
                .uri("/99")
                .retrieve()
                .toEntity(ProductResponseDto.class));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}

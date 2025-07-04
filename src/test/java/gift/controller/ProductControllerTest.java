package gift.controller;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @Autowired
    private ProductService productService;

    @LocalServerPort
    int port;

    private RestClient client = RestClient.builder().build();

    @Test
    void 정상적인상품저장() {
        var url = "http://localhost:" + port + "/api/products";
        ProductRequestDto requestDto = new ProductRequestDto(
                "인형",
                39900L,
                "http://example.png"
        );

        var response = client.post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        ProductResponseDto dtoBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assert dtoBody != null;
        assertThat(dtoBody.getName()).isEqualTo("인형");
        assertThat(dtoBody.getPrice()).isEqualTo(39900L);
        assertThat(dtoBody.getImageUrl()).isEqualTo("http://example.png");
        assertThat(dtoBody.getApproved()).isEqualTo(true);
    }


    @Test
    void 상품길이가15자를초과하는경우의상품저장() {
        var url = "http://localhost:" + port + "/api/products";
        ProductRequestDto requestDto = new ProductRequestDto(
                "이것은이름이겁나게길고긴상품이름",
                9900L,
                "http://example.png"
        );

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                    () ->
                        client.post()
                            .uri(url)
                            .body(requestDto)
                            .retrieve()
                            .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String body = ex.getResponseBodyAsString();
                    assertThat(body).contains("상품명은 최대 15자를 가질 수 있습니다.");
                });
    }

    @Test
    void 허용하지않는특수문자가포함된상품저장() {
        var url = "http://localhost:" + port + "/api/products";
        ProductRequestDto requestDto = new ProductRequestDto(
                "@@상품이름@@",
                9900L,
                "http://example.png"
        );

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String body = ex.getResponseBodyAsString();
                    assertThat(body).contains("상품명에 허용하지 않는 특수문자가 포함되어 있습니다. 허용하는 특수문자: (), [], +, -, &, /, _");
                });
    }

    @Test
    void 상품가격이음수인상품저장() {
        var url = "http://localhost:" + port + "/api/products";
        ProductRequestDto requestDto = new ProductRequestDto(
                "인형",
                -10000L,
                "http://example.png"
        );

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String body = ex.getResponseBodyAsString();
                    assertThat(body).contains("가격은 0원 이상입니다.");
                });
    }

    @Test
    void 잘못된이미지URL을가진상품저장() {
        var url = "http://localhost:" + port + "/api/products";
        ProductRequestDto requestDto = new ProductRequestDto(
                "인형",
                10000L,
                "example.png"
        );

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(Void.class)
                )
                .satisfies(ex -> {
                    String body = ex.getResponseBodyAsString();
                    assertThat(body).contains("유효한 이미지 URL 형식이 아닙니다.");
                });
    }

    @Test
    void 카카오문구가포함된상품저장() {
        var url = "http://localhost:" + port + "/api/products";
        ProductRequestDto requestDto = new ProductRequestDto(
                "카카오 인형",
                39900L,
                "http://example.png"
        );

        var response = client.post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        ProductResponseDto dtoBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assert dtoBody != null;
        assertThat(dtoBody.getApproved()).isEqualTo(false);
        assertThat(dtoBody.getDescription()).isEqualTo("카카오 문구가 담긴 상품은 담당 MD와 협의 후 사용가능합니다.");
    }

    @Test
    void 카카오문구가포함된상품수정() {
        var url = "http://localhost:" + port + "/api/products/1";
        ProductRequestDto requestDto = new ProductRequestDto(
                "카카오 인형",
                39900L,
                "http://example.png"
        );

        var response = client.put()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        ProductResponseDto dtoBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assert dtoBody != null;
        assertThat(dtoBody.getApproved()).isEqualTo(false);
        assertThat(dtoBody.getDescription()).isEqualTo("카카오 문구가 담긴 상품은 담당 MD와 협의 후 사용가능합니다.");
    }


}

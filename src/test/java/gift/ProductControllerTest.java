package gift;

import gift.dto.CreateProductRequestDto;
import gift.dto.UpdateProductRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {
    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();

    @Test
    void 길이_제한을_넘은_상품이름으로_등록하면_400을_반환한다() {
        var url = "http://localhost:" + port + "/api/products";
        // 상품 이름이 16자
        CreateProductRequestDto requestDto = new CreateProductRequestDto(
                "123456789 123456", 1200L, "test.jpg"
        );

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(Void.class));
    }

    @Test
    void 길이_제한을_넘은_상품이름으로_수정하면_400을_반환한다() {
        var url = "http://localhost:" + port + "/api/products/1";
        // 상품 이름이 16자
        UpdateProductRequestDto requestDto = new UpdateProductRequestDto(
                1L, "123456789 123456", 1200L, "test.jpg"
        );

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                client.put()
                                        .uri(url)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(Void.class));

    }

}

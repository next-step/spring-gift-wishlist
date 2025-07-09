package gift;

import gift.component.JwtUtil;
import gift.dto.CreateProductRequestDto;
import gift.dto.UpdateProductRequestDto;
import gift.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {
    @LocalServerPort
    private int port;

    @MockitoBean
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        doNothing().when(jwtUtil).validateAuthorizationHeader(anyString(), anyString());
    }

    private RestClient client = RestClient.builder().build();

    // E2E 테스트
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

    @Test
    void 허용되지_않은_특수_문자로_상품이름을_등록할_경우_400을_반환한다() {
        var url = "http://localhost:" + port + "/api/products";
        // 상품 이름에 허용되지 않은 특수문자 사용
        CreateProductRequestDto requestDto = new CreateProductRequestDto(
                "쌍쌍바!", 1200L, "test.jpg"
        );

        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(Void.class));
    }

    @Test
    void 허용되지_않은_특수_문자로_상품이름을_수정할_경우_400을_반환한다() {
        var url = "http://localhost:" + port + "/api/products/1";
        // 상품 이름에 허용되지 않은 특수문자 사용\
        UpdateProductRequestDto requestDto = new UpdateProductRequestDto(
                1L, "쌍쌍바!", 1200L, "test.jpg"
        );

        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(
                        () ->
                                client.put()
                                        .uri(url)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(Void.class));
    }

    @Test
    void 허용된_문자로_상품이름을_등록할_경우_201을_반환한다() {
        var url = "http://localhost:" + port + "/api/products";
        // 상품 이름에 허용되지 않은 특수문자 사용\
        CreateProductRequestDto requestDto = new CreateProductRequestDto(
                "[쌍쌍바]", 1200L, "test.jpg"
        );

        var response = client.post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 허용된_문자로_상품이름을_수정할_경우_204을_반환한다() {
        var url = "http://localhost:" + port + "/api/products/1";
        // 상품 이름에 허용되지 않은 특수문자 사용\
        UpdateProductRequestDto requestDto = new UpdateProductRequestDto(
                1L, "[쌍쌍바]", 1200L, "test.jpg"
        );

        var response = client.put()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void MD의_승인이_없을_때_카카오를_포함한_상품이름으로_등록할_경우_400을_반환한다() {
        var url = "http://localhost:" + port + "/api/products";
        CreateProductRequestDto requestDto = new CreateProductRequestDto(
                "[카카오] 쌍쌍바", 1200L, "test.jpg");

        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(Void.class));
    }

    @Test
    void MD의_승인이_없을_때_카카오를_포함한_상품이름으로_수정할_경우_400을_반환한다() {
        var url = "http://localhost:" + port + "/api/products/1";
        UpdateProductRequestDto requestDto = new UpdateProductRequestDto(
                1L, "[카카오] 쌍쌍바", 1200L, "test.jpg");

        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(
                        () ->
                                client.put()
                                        .uri(url)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(Void.class));
    }

    // 단위 테스트
    @Test
    void 카카오가_포함된_이름은_예외를_던진다() {
        assertThatThrownBy(() -> Product.of(null, "[카카오] 메로나", 1200L, "img.jpg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카카오");
    }

    @Test
    void 허용되지_않은_문자가_포함된_이름은_예외를_던진다() {
        assertThatThrownBy(() -> Product.of(null, "메로나!", 1000L, "img.jpg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("특수 문자");
    }

    @Test
    void 상품_가격이_음수인_경우_예외를_던진다() {
        assertThatThrownBy(() -> Product.of(null, "메로나", -12L, "img.jpg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격");
    }
}

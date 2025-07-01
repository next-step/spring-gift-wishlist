package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import gift.dto.request.ProductCreateRequestDto;
import gift.dto.response.ProductCreateResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @LocalServerPort
    private int port;

    private RestClient restClient = RestClient.builder().build();

    // POST
    @Test
    void 단건상품등록_CREATED_테스트() {
        // given
        var url = "http://localhost:" + port + "/api/products";

        var request = new ProductCreateRequestDto(
            "아이스 카페 아메리카노 T",
            4500.0,
            "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
        );

        // when
        var response = restClient.post()
            .uri(url)
            .body(request)
            .retrieve()
            .toEntity(ProductCreateResponseDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var actual = response.getBody();

        assertThat(actual.name()).isEqualTo(request.name());
        assertThat(actual.price()).isEqualTo(request.price());
        assertThat(actual.imageUrl()).isEqualTo(request.imageUrl());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "12345 12345 12345",   // 숫자 16자
        "Abcde fghij klmno",   // 영어 16자
        "일이삼사오 일이삼사오 일이삼사오",   // 한글 16자
        "콜라@맛!",            // 허용되지 않은 특수문자
        "카카오커피"           // '카카오' 포함
    })
    void 단건상품등록_BAD_REQUEST_상품이름_유효성_검사(String invalidName) {
        //given
        var url = "http://localhost:" + port + "/api/products";

        var request = new ProductCreateRequestDto(
            invalidName,
            4500.0,
            "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
        );

        // when & then
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(
                () -> restClient.post()
                    .uri(url)
                    .body(request)
                    .retrieve()
                    .toEntity(ProductCreateResponseDto.class)
            );
    }
}
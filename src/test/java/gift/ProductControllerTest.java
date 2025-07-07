package gift;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.UpdateProductRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {
    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();
    private String url;

    @BeforeEach
    void setUp() {
        url = "http://localhost:" + port + "/api";
    }

    @Test
    void 올바른_상품_생성() {
        ProductRequestDto productRequestDto = new ProductRequestDto(
                "치킨",
                10000,
                "https://picsum.photos/200",
                false
        );

        var response = client.post()
                .uri(url + "/products")
                .body(productRequestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().name()).isEqualTo("치킨")
        );
    }

    @Test
    void MD승인_없이_카카오_이름이_들어간_상품_생성() {
        ProductRequestDto productRequestDto = new ProductRequestDto(
                "카카오치킨",
                10000,
                "https://picsum.photos/200",
                false
        );

        assertThatThrownBy(() ->
                client.post()
                        .uri(url + "/products")
                        .body(productRequestDto)
                        .retrieve()
                        .toBodilessEntity()
        ).isInstanceOf(HttpClientErrorException.BadRequest.class);
    }

    @Test
    void MD승인_받고_카카오_이름이_들어간_상품_생성() {
        ProductRequestDto productRequestDto = new ProductRequestDto(
                "카카오치킨",
                10000,
                "https://picsum.photos/200",
                true
        );

        var response = client.post()
                .uri(url + "/products")
                .body(productRequestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().name()).isEqualTo("카카오치킨")
        );
    }

    @Test
    void MD승인_없이_카카오_이름으로_상품_수정() {
        ProductRequestDto productRequestDto = new ProductRequestDto(
                "치킨",
                10000,
                "https://picsum.photos/200",
                false
        );

        var response = client.post()
                .uri(url + "/products")
                .body(productRequestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getBody()).isNotNull();
        Long productId = response.getBody().id();

        UpdateProductRequestDto updateProductRequestDto = new UpdateProductRequestDto(
                productId,
                "카카오치킨",
                10000,
                "https://picsum.photos/200",
                false
        );

        assertThatThrownBy(() ->
                client.put()
                        .uri(url + "/products/" + productId)
                        .body(updateProductRequestDto)
                        .retrieve()
                        .toBodilessEntity()
        ).isInstanceOf(HttpClientErrorException.BadRequest.class);
    }

    @Test
    void 정상_회원가입() {
        var memberRequestDto = new MemberRequestDto(
                "test@example.com",
                "test123");
        var response = client.post()
                .uri(url + "/members/register")
                .body(memberRequestDto)
                .retrieve()
                .toEntity(MemberResponseDto.class);
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().token()).isNotBlank()
        );
    }

    @Test
    void 비밀번호_잘못된_로그인() {
        var registerRequestDto = new MemberRequestDto(
                "test@example.com",
                "test123");
        var response = client.post()
                .uri(url + "/members/register")
                .body(registerRequestDto)
                .retrieve()
                .toEntity(MemberResponseDto.class);

        var loginRequestDto = new MemberRequestDto(
                "test@example.com",
                "wrong_password");

        assertThatThrownBy(() ->
                client.post()
                        .uri(url+ "/members/login")
                        .body(loginRequestDto)
                        .retrieve()
                        .toBodilessEntity()
        ).isInstanceOf(HttpClientErrorException.Forbidden.class);
    }
}

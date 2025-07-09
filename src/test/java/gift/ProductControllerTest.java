package gift;

import gift.dto.CreateProductRequest;
import gift.dto.UpdateProductRequest;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Role;
import gift.repository.MemberRepository;
import gift.token.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/clear_product_table.sql", "/insert_product_table.sql", "/clear_member_table.sql"})
public class ProductControllerTest {

    private final String baseUrl = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private RestClient restClient;
    private String mdToken;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create();

        // MD 계정 생성
        Member user = new Member(2L, "user@example.com", "userpassword123456789", Role.ROLE_MD);

        // MD 토큰 생성
        mdToken = jwtTokenProvider.createToken(user);
    }

    @Test
    void 유효한_생성_시_201_CREATED() {
        String url = baseUrl + port + "/api/products";
        CreateProductRequest requestDto = new CreateProductRequest(
                "아이스 카페 아메리카노 T",
                4700,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
        );
        ResponseEntity<Product> response = restClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + mdToken)
                .body(requestDto)
                .retrieve()
                .toEntity(Product.class);
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getBody().getName()).isEqualTo("아이스 카페 아메리카노 T")
        );
    }

    @Test
    void 유효하지_않은_생성_시_400_BAD_REQUEST() {
        String url = baseUrl + port + "/api/products";
        CreateProductRequest requestDto = new CreateProductRequest(null, null, null);
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                restClient.post()
                                        .uri(url)
                                        .header("Authorization", "Bearer " + mdToken)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(Void.class)
                );
    }

    @Test
    void 유효한_조회_시_200_OK() {
        String url = baseUrl + port + "/api/products/1";
        ResponseEntity<Product> response = restClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + mdToken)
                .retrieve()
                .toEntity(Product.class);
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody().getName()).isEqualTo("아이스 카페 아메리카노 T")
        );
    }

    @Test
    void 유효하지_않은_조회_시_404_NOT_FOUND() {
        String url = baseUrl + port + "/api/products/0";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () ->
                                restClient.get()
                                        .uri(url)
                                        .header("Authorization", "Bearer " + mdToken)
                                        .retrieve()
                                        .toEntity(Void.class)
                );
    }

    @Test
    void 유효한_수정_시_200_OK() {
        String url = baseUrl + port + "/api/products/1";
        UpdateProductRequest patchDto = new UpdateProductRequest(
                "각하오 커피",
                7800,
                null
        );
        ResponseEntity<Product> response = restClient.patch()
                .uri(url)
                .header("Authorization", "Bearer " + mdToken)
                .body(patchDto)
                .retrieve()
                .toEntity(Product.class);
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody().getName()).isEqualTo("각하오 커피"),
                () -> assertThat(response.getBody().getImageUrl()).isNotNull()
        );
    }

    @Test
    void 유효하지_않은_수정_시_400_BAD_REQUEST() {
        String url = baseUrl + port + "/api/products/1";
        UpdateProductRequest patchDto = new UpdateProductRequest(null, null, null);
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                restClient.patch()
                                        .uri(url)
                                        .header("Authorization", "Bearer " + mdToken)
                                        .body(patchDto)
                                        .retrieve()
                                        .toEntity(Product.class)
                );
    }

    @Test
    void 리스트_조회_시_200_OK() {
        String url = baseUrl + port + "/api/products";
        ResponseEntity<List<Product>> response = restClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + mdToken)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<Product>>() {});

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody().size()).isNotZero()
        );
    }

    @Test
    void 유효한_삭제_시_204_NO_CONTENT() {
        String url = baseUrl + port + "/api/products/1";
        ResponseEntity<Void> response = restClient.delete()
                .uri(url)
                .header("Authorization", "Bearer " + mdToken)
                .retrieve()
                .toEntity(Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void 유효하지_않은_삭제_시_404_NOT_FOUND() {
        String url = baseUrl + port + "/api/products/0";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () ->
                                restClient.delete()
                                        .uri(url)
                                        .header("Authorization", "Bearer " + mdToken)
                                        .retrieve()
                                        .toEntity(Void.class)
                );
    }


}

package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import gift.dto.request.ApprovedProductCreateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApprovedProductControllerTest {

    @LocalServerPort
    private int port;

    private final RestClient client = RestClient.builder().build();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/admin/approved-products";
    }

    private <T> ResponseEntity<T> exchange(HttpMethod method,
        String url,
        Object body,
        ParameterizedTypeReference<T> type) {

        if (body == null) {
            return client.method(method)
                .uri(url)
                .retrieve()
                .toEntity(type);
        }

        return client.method(method)
            .uri(url)
            .body(body)
            .retrieve()
            .toEntity(type);
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM approved_products");
        jdbcTemplate.execute(
            "ALTER TABLE approved_products ALTER COLUMN id RESTART WITH 1");

        String sql = "INSERT INTO approved_products(name) VALUES (?)";
        jdbcTemplate.update(sql, "카카오");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "카카오 프렌즈",
        "The 카카오"
    })
    void 협의된상품등록_CREATED_테스트(String validName) {
        // given
        var request = new ApprovedProductCreateRequestDto(
            validName
        );

        // when
        var response = exchange(HttpMethod.POST, baseUrl(), request,
            new ParameterizedTypeReference<Void>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var actualName = jdbcTemplate.queryForObject(
            "SELECT * FROM approved_products WHERE name = ?",
            (rs, rowNum) -> rs.getString("name"),
            validName
        );

        assertThat(actualName).isEqualTo(validName);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "  ",
        "카카오123451234512345",   // 15자 초과
        "카카오@",                 // 허용되지 않은 특수문자
        "상품"                     // 카카오 누락
    })
    void 협의된상품등록_BAD_REQUEST_상품이름_유효성_검사(String validName) {
        // given
        var request = new ApprovedProductCreateRequestDto(
            validName
        );

        // when & then
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(
                () -> exchange(HttpMethod.POST, baseUrl(), request,
                    new ParameterizedTypeReference<Void>() {
                    })
            );
    }

    @Test
    void 협의된상품등록_BAD_REQUEST_상품이름_중복() {
        // given
        var request = new ApprovedProductCreateRequestDto(
            "카카오"
        );

        // when & then
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(
                () -> exchange(HttpMethod.POST, baseUrl(), request,
                    new ParameterizedTypeReference<Void>() {
                    })
            );
    }
}
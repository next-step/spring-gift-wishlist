package gift.wish.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gift.member.security.JwtTokenProvider;
import gift.wish.dto.WishCreateRequestDto;
import gift.wish.dto.WishCreateResponseDto;
import gift.wish.dto.WishPageResponseDto;
import gift.wish.entity.Wish;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class WishControllerTest {

    @LocalServerPort
    private int port;

    private final RestClient client = RestClient.builder().build();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    String userToken;
    String adminToken;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/wishes";
    }

    private Wish queryWishById(int id) {
        return jdbcTemplate.queryForObject(
            "SELECT wishId, memberId, productId, createdDate FROM wishes WHERE wishId = ?",
            (rs, rowNum) -> new Wish(
                rs.getLong("wishId"),
                rs.getLong("memberId"),
                rs.getLong("productId"),
                rs.getTimestamp("createdDate").toLocalDateTime()),
            id
        );
    }

    Stream<String> tokenProvider() {
        return Stream.of(userToken, adminToken);
    }

    @BeforeAll
    void beforeAll() {
        jdbcTemplate.execute("DELETE FROM members");
        jdbcTemplate.execute("ALTER TABLE members ALTER COLUMN memberId RESTART WITH 1");

        String sql = "INSERT INTO members(email, password, name, role) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, "user@email.com", "1234", "user", "ROLE_USER");
        jdbcTemplate.update(sql, "admin@email.com", "1234", "admin", "ROLE_ADMIN");

        userToken = jwtTokenProvider.generateToken(1L, "user@email.com", "ROLE_USER");
        adminToken = jwtTokenProvider.generateToken(2L, "admin@email.com", "ROLE_ADMIN");

        // product
        jdbcTemplate.execute("DELETE FROM products");
        jdbcTemplate.execute("ALTER TABLE products ALTER COLUMN productId RESTART WITH 1");

        String productSql = "INSERT INTO products(name, price, imageUrl, mdConfirmed) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(productSql, "one", "1", "https://1.img", "false");
        jdbcTemplate.update(productSql, "two", "2", "https://2.img", "false");
        jdbcTemplate.update(productSql, "three", "3", "https://3.img", "false");
    }

    @BeforeEach
    void setUp() {
        // wish
        jdbcTemplate.execute("DELETE FROM wishes");
        jdbcTemplate.execute("ALTER TABLE wishes ALTER COLUMN wishId RESTART WITH 1");

        String wishSql = "INSERT INTO wishes(memberId, productId) VALUES (?, ?)";
        jdbcTemplate.update(wishSql, "1", "1");
        jdbcTemplate.update(wishSql, "2", "1");
    }

    // POST
    @ParameterizedTest
    @MethodSource("tokenProvider")
    void 위시상품추가_CREATED_테스트(String token) {
        // given
        var request = new WishCreateRequestDto(2L);

        // when
        var response = client.post()
            .uri(baseUrl())
            .headers(headers -> headers.setBearerAuth(token))
            .body(request)
            .retrieve()
            .toEntity(WishCreateResponseDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    // GET
    @ParameterizedTest
    @MethodSource("tokenProvider")
    void 위시상품조회_OK_테스트(String token) {
        // given & when
        var response = client.get()
            .uri(baseUrl() + "?page=0&size=10&createdDate,desc")
            .headers(headers -> headers.setBearerAuth(token))
            .retrieve()
            .toEntity(WishPageResponseDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var actual = response.getBody();
        System.out.println(actual);
    }

    // DELETE
    @Test
    void 위시상품삭제_NO_CONTENT_테스트() {
        // given & when
        var response = client.delete()
            .uri(baseUrl() + "/1")
            .headers(headers -> headers.setBearerAuth(userToken))
            .retrieve()
            .toEntity(Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


}
package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import gift.dto.request.ProductCreateRequestDto;
import gift.dto.request.ProductUpdateRequestDto;
import gift.dto.response.ProductCreateResponseDto;
import gift.dto.response.ProductGetResponseDto;
import gift.entity.Product;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @LocalServerPort
    private int port;

    private final RestClient restClient = RestClient.builder().build();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // products TABLE
        jdbcTemplate.execute("DELETE FROM products");
        jdbcTemplate.execute("ALTER TABLE products ALTER COLUMN productId RESTART WITH 1");

        String sql = "INSERT INTO products(name, price, imageUrl) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, "one", "4500", "https://img.com/BeforeEach.jpg");
        jdbcTemplate.update(sql, "two", "4500", "https://img.com/BeforeEach.jpg");
        jdbcTemplate.update(sql, "three", "4500", "https://img.com/BeforeEach.jpg");

        // approved_product_names TABLE
        jdbcTemplate.execute("DELETE FROM approved_products");
        jdbcTemplate.execute(
            "ALTER TABLE approved_products ALTER COLUMN id RESTART WITH 1");

        String approvedProductSql = "INSERT INTO approved_products(name) VALUES (?)";
        jdbcTemplate.update(approvedProductSql, "카카오");
    }

    // POST
    @Test
    void 단건상품등록_CREATED_테스트() {
        // given
        var url = "http://localhost:" + port + "/api/products";

        var request = new ProductCreateRequestDto(
            "PostCreated",
            4500.0,
            "https://PostCreated.jpg"
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
        "",
        " ",
        "123451234512345",            // 숫자 15자
        "Abcdefghijklmno",            // 영어 15자
        "일이삼사오일이삼사오일이삼사오",  // 한글 15자
        "()[]+-&/_",                  // 허용되는 특수문자
        "카카오"                 // 협의된 '카카오' 포함
    })
    void 단건상품등록_CREATED_상품이름_유효성_검사(String validName) {
        // given
        var url = "http://localhost:" + port + "/api/products";

        var request = new ProductCreateRequestDto(
            validName,
            4500.0,
            "https://ValidName.jpg"
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
        "12345 12345 12345",             // 숫자 17자
        "Abcde fghij klmno",             // 영어 17자
        "일이삼사오 일이삼사오 일이삼사오",   // 한글 17자
        "콜라@맛!",                       // 허용되지 않은 특수문자
        "카카오커피"                       // 협의되지 않은 '카카오' 포함
    })
    void 단건상품등록_BAD_REQUEST_상품이름_유효성_검사(String invalidName) {
        //given
        var url = "http://localhost:" + port + "/api/products";

        var request = new ProductCreateRequestDto(
            invalidName,
            4500.0,
            "https://invalidName.jpg"
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

    // GET
    @Test
    void 전체상품조회_OK_테스트() {
        // given
        var url = "http://localhost:" + port + "/api/products";

        // when
        var response = restClient.get()
            .uri(url)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<List<ProductGetResponseDto>>() {
            });

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var actual = response.getBody();
        System.out.println(actual);
    }

    @Test
    void 단건상품조회_OK_테스트() {
        // given
        var url = "http://localhost:" + port + "/api/products/1";

        // when
        var response = restClient.get()
            .uri(url)
            .retrieve()
            .toEntity(ProductGetResponseDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var actual = response.getBody();
        System.out.println(actual);
    }

    @Test
    void 단건상품조회_NOT_FOUND_데이터베이스_상품존재() {
        // given
        var url = "http://localhost:" + port + "/api/products/321";

        // when
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
            .isThrownBy(
                () -> restClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(ProductGetResponseDto.class)
            );
    }

    // PUT
    @Test
    void 단건상품수정_NO_CONTENT_테스트() {
        // given
        var url = "http://localhost:" + port + "/api/products/1";

        var request = new ProductUpdateRequestDto(
            "수정된 상품",
            10000.0,
            "https://PutNoContent.jpg"
        );

        // when
        var response = restClient.put()
            .uri(url)
            .body(request)
            .retrieve()
            .toEntity(Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Product result = jdbcTemplate.queryForObject(
            "SELECT name, price, imageUrl FROM products WHERE productId = 1",
            (rs, rowNum) -> new Product(
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getString("imageUrl")
            )
        );

        assertThat(result.getName()).isEqualTo(request.name());
        assertThat(result.getPrice()).isEqualTo(request.price());
        assertThat(result.getImageUrl()).isEqualTo(request.imageUrl());

    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        " ",
        "123451234512345",            // 숫자 15자
        "Abcdefghijklmno",            // 영어 15자
        "일이삼사오일이삼사오일이삼사오",  // 한글 15자
        "()[]+-&/_",                  // 허용되는 특수문자
        "카카오"                       // 협의된 '카카오' 포함
    })
    void 단건상품수정_NO_CONTENT_상품이름_유효성_검사(String validName) {
        // given
        var url = "http://localhost:" + port + "/api/products/1";

        var request = new ProductUpdateRequestDto(
            validName,
            10000.0,
            "https://validName.jpg"
        );

        // when
        var response = restClient.put()
            .uri(url)
            .body(request)
            .retrieve()
            .toEntity(Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Product result = jdbcTemplate.queryForObject(
            "SELECT name, price, imageUrl FROM products WHERE productId = 1",
            (rs, rowNum) -> new Product(
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getString("imageUrl")
            )
        );

        assertThat(result.getName()).isEqualTo(request.name());
        assertThat(result.getPrice()).isEqualTo(request.price());
        assertThat(result.getImageUrl()).isEqualTo(request.imageUrl());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "12345 12345 12345",             // 숫자 17자
        "Abcde fghij klmno",             // 영어 17자
        "일이삼사오 일이삼사오 일이삼사오",   // 한글 17자
        "콜라@맛!",                       // 허용되지 않은 특수문자
        "카카오커피"                       // 협의되지 않은 '카카오' 포함
    })
    void 단건상품수정_BAD_REQUEST_상품이름_유효성_검사(String invalidName) {
        //given
        var url = "http://localhost:" + port + "/api/products/1";

        var request = new ProductUpdateRequestDto(
            invalidName,
            10000.0,
            "https://invalidName.jpg"
        );

        // when & then
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
            .isThrownBy(
                () -> restClient.put()
                    .uri(url)
                    .body(request)
                    .retrieve()
                    .toEntity(Void.class)
            );
    }

    // DELETE
    @Test
    void 단건상품삭제_NO_CONTENT_테스트() {
        // given
        var url = "http://localhost:" + port + "/api/products/1";

        // when
        var response = restClient.delete()
            .uri(url)
            .retrieve()
            .toEntity(Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        var results = jdbcTemplate.query(
            "SELECT * FROM products WHERE productId = 1",
            (rs, rowNum) -> rs.getInt("productId")
        );

        assertThat(results).isEmpty();
    }

    @Test
    void 단건상품삭제_NOT_FOUND_데이터베이스_상품존재() {
        // given
        var url = "http://localhost:" + port + "/api/products/321";

        // when
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
            .isThrownBy(
                () -> restClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(Void.class)
            );
    }

}
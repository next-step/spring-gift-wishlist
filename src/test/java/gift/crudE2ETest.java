package gift;

import gift.product.dto.ProductDto;
import gift.product.repository.ProductDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class crudE2ETest {
    @LocalServerPort
    private int port;
    private String firstUUID;

    private RestClient restClient = RestClient.builder().build();

    @Autowired
    ProductDao productDao;

    @BeforeEach
    void setUp() {
        ProductDto productDto = new ProductDto("testProduct1", 1000, "imageUrl1");
        productDao.save(productDto);
        firstUUID = productDao.findAll().getFirst().getId();
    }

    @Test
    void productCreateTest() {
        String url = "http://localhost:" + port + "/api/product/add";
        ProductDto requestDto = new ProductDto("testProduct2", 2000, "imageUrl2");
        ResponseEntity<ProductDto> response = restClient
                .post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(ProductDto.class);

        ProductDto expectedDto = requestDto;
        assertThat(response.getBody())
                .extracting("name", "price", "imageUrl")
                .containsExactly(expectedDto.getName(), expectedDto.getPrice(), expectedDto.getImageUrl());
    }

    @Test
    void productCreateWithSpecialCharacterTest() {
        String url = "http://localhost:" + port + "/api/product/add";
        ProductDto requestDto = new ProductDto("testProduct2!?", 2000, "imageUrl2");
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () -> restClient
                                .post()
                                .uri(url)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(String.class)
                )
                .extracting(RestClientResponseException::getResponseBodyAsString)
                .asString()
                .isEqualTo("name : 알파벳, 한글, 숫자, (, ), [, ], +, -, &, /, _ 만 입력 가능합니다\n");
    }

    @Test
    void productReadTest() {
        String url = "http://localhost:" + port + "/api/product/" + firstUUID;
        ResponseEntity<ProductDto> response = restClient
                .get()
                .uri(url)
                .retrieve()
                .toEntity(ProductDto.class);

        assertThat(response.getBody())
                .extracting("name", "price", "imageUrl")
                .containsExactly("testProduct1", 1000, "imageUrl1");
    }

    @Test
    void productReadNotFoundTest() {
        String url = "http://localhost:" + port + "/api/product/" + "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () -> restClient
                                .get()
                                .uri(url)
                                .retrieve()
                                .toEntity(String.class)
                )
                .extracting(RestClientResponseException::getResponseBodyAsString)
                .asString()
                .isEqualTo("DB에서 해당 ID를 찾을 수 없습니다.");
    }

    @Test
    void productUpdateTest() {
        String url = "http://localhost:" + port + "/api/product/" + firstUUID + "/update";
        ProductDto requestDto = new ProductDto("updatedName", 10, "updatedUrl");
        ResponseEntity<ProductDto> response = restClient
                .patch()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(ProductDto.class);

        assertThat(response.getBody())
                .extracting("name", "price", "imageUrl")
                .containsExactly("updatedName", 10, "updatedUrl");
    }

    @Test
    void productUpdateNotFoundTest() {
        String url = "http://localhost:" + port + "/api/product/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx/update";
        ProductDto requestDto = new ProductDto("updatedName", 10, "updatedUrl");
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () -> restClient
                                .patch()
                                .uri(url)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(String.class)
                )
                .extracting(RestClientResponseException::getResponseBodyAsString)
                .asString()
                .isEqualTo("DB에서 해당 ID를 찾을 수 없습니다.");
    }

    @Test
    void productDeleteTest() {
        String url = "http://localhost:" + port + "/api/product/" + firstUUID + "/delete";
        ResponseEntity<ProductDto> response = restClient
                .delete()
                .uri(url)
                .retrieve()
                .toEntity(ProductDto.class);

        assertThat(response.getBody())
                .extracting("name", "price", "imageUrl")
                .containsExactly("testProduct1", 1000, "imageUrl1");
    }

    @Test
    void productDeleteNotFoundTest() {
        String url = "http://localhost:" + port + "/api/product/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx/delete";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () -> restClient
                                .delete()
                                .uri(url)
                                .retrieve()
                                .toEntity(String.class)
                )
                .extracting(RestClientResponseException::getResponseBodyAsString)
                .asString()
                .isEqualTo("DB에서 해당 ID를 찾을 수 없습니다.");
    }
}

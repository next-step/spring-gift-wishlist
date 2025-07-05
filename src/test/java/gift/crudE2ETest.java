package gift;

import gift.product.dto.RequestDto;
import gift.product.dto.ResponseDto;
import gift.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class crudE2ETest {
    @LocalServerPort
    private int port;
    private UUID lastUUID;

    private RestClient restClient = RestClient.builder().build();

    @Autowired
    ProductService productService;

    @BeforeEach
    void setUp() {
        RequestDto requestDto = new RequestDto("testProduct1", 1000, "imageUrl1");
        productService.saveProduct(requestDto);
        lastUUID = productService.findAll().getLast().getId();
    }

    @Test
    void productCreateTest() {
        String url = "http://localhost:" + port + "/api/product/add";
        RequestDto requestDto = new RequestDto("testProduct2", 2000, "imageUrl2");
        ResponseEntity<ResponseDto> response = restClient
                .post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(ResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody())
                .extracting("name", "price", "imageUrl")
                .containsExactly(requestDto.getName(), requestDto.getPrice(), requestDto.getImageUrl());
    }

    @Test
    void productCreateWithSpecialCharacterTest() {
        String url = "http://localhost:" + port + "/api/product/add";
        RequestDto requestDto = new RequestDto("testProduct2!?", 2000, "imageUrl2");
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
        String url = "http://localhost:" + port + "/api/product/" + lastUUID;
        ResponseEntity<ResponseDto> response = restClient
                .get()
                .uri(url)
                .retrieve()
                .toEntity(ResponseDto.class);

        assertThat(response.getBody())
                .extracting("name", "price", "imageUrl")
                .containsExactly("testProduct1", 1000, "imageUrl1");
    }

    @Test
    void productReadNotFoundTest() {
        String url = "http://localhost:" + port + "/api/product/" + "00000000-0000-0000-0000-000000000000";
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
        String url = "http://localhost:" + port + "/api/product/" + lastUUID + "/update";
        RequestDto requestDto = new RequestDto("updatedName", 10, "updatedUrl");
        ResponseEntity<ResponseDto> response = restClient
                .patch()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(ResponseDto.class);

        assertThat(response.getBody())
                .extracting("name", "price", "imageUrl")
                .containsExactly("updatedName", 10, "updatedUrl");
    }

    @Test
    void productUpdateNotFoundTest() {
        String url = "http://localhost:" + port + "/api/product/00000000-0000-0000-0000-000000000000/update";
        RequestDto requestDto = new RequestDto("updatedName", 10, "updatedUrl");
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
        String url = "http://localhost:" + port + "/api/product/" + lastUUID + "/delete";
        ResponseEntity<ResponseDto> response = restClient
                .delete()
                .uri(url)
                .retrieve()
                .toEntity(ResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void productDeleteNotFoundTest() {
        String url = "http://localhost:" + port + "/api/product/00000000-0000-0000-0000-000000000000/delete";
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

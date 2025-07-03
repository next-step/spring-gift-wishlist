package gift.controller;

import gift.dto.ErrorResponseDto;
import gift.dto.ProductAddRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void 상품명을_입력하지_않았을_때_400을_반환() throws Exception {
        String url = "http://localhost:" + port + "/api/products";

        ProductAddRequestDto requestDto = new ProductAddRequestDto("", 500L, "https://example.com/img.jpg");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductAddRequestDto> request = new HttpEntity<>(requestDto, headers);

        ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url, request, ErrorResponseDto.class);

        ErrorResponseDto responseDto = response.getBody();
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.status()).isEqualTo(400);
    }

    @Test
    void 상품명을_15자_초과하여_입력했을_때_400을_반환() throws Exception {
        String url = "http://localhost:" + port + "/api/products";

        ProductAddRequestDto requestDto = new ProductAddRequestDto("123456789101112131415", 500L, "https://example.com/img.jpg");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductAddRequestDto> request = new HttpEntity<>(requestDto, headers);

        ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url, request, ErrorResponseDto.class);

        ErrorResponseDto responseDto = response.getBody();
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.status()).isEqualTo(400);
    }

    @Test
    void 상품명에_카카오_문자를_포함하여_입력했을_때_400을_반환() throws Exception {
        String url = "http://localhost:" + port + "/api/products";

        ProductAddRequestDto requestDto = new ProductAddRequestDto("카카오 초콜릿", 500L, "https://example.com/img.jpg");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductAddRequestDto> request = new HttpEntity<>(requestDto, headers);

        ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url, request, ErrorResponseDto.class);

        ErrorResponseDto responseDto = response.getBody();
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.status()).isEqualTo(400);
    }

    @Test
    void 상품명에_쓸수없는_특수문자를_포함하여_입력했을_때_400을_반환() throws Exception {
        String url = "http://localhost:" + port + "/api/products";

        ProductAddRequestDto requestDto = new ProductAddRequestDto("쓸수없는특수문자*", 500L, "https://example.com/img.jpg");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductAddRequestDto> request = new HttpEntity<>(requestDto, headers);

        ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(url, request, ErrorResponseDto.class);

        ErrorResponseDto responseDto = response.getBody();
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.status()).isEqualTo(400);
    }


}

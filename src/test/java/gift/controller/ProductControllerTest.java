package gift.controller;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.dto.CreateProductRequestDto;
import gift.entity.Product;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();

    @Test
    void 전체_조회하면_200이_반환된다() {
        String url = "http://localhost:" + port + "/api/products";
        ResponseEntity<List<Product>> response = client.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<Product>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 존재하는_아이디로_개별조회하면_200이_반환된다() {
        String url = "http://localhost:" + port + "/api/products/1";
        ResponseEntity<Product> response = client.get()
                .uri(url)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 존재하지_않는_아이디로_개별조회하면_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/999";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.get()
                                .uri(url)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    void MD가_승인하지_않은_아이디로_개별조회하면_403가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/2";
        assertThatExceptionOfType(HttpClientErrorException.Forbidden.class)
                .isThrownBy(() ->
                        client.get()
                                .uri(url)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    void 상품등록에_성공하면_201가_반환된다() {
        String url = "http://localhost:" + port + "/api/products";
        CreateProductRequestDto requestDto = new CreateProductRequestDto("asd", 123L, "aasdfgh");
        ResponseEntity<Product> response = client.post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 상품등록에_실패하면_400가_반환된다() {
        String url = "http://localhost:" + port + "/api/products";
        CreateProductRequestDto requestDto = new CreateProductRequestDto("asdasdasdasdasdasdasdasdasdasd", 123L, "aasdfgh");
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        client.post()
                                .uri(url)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    void 상품수정에_실패하면_400가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/1";
        CreateProductRequestDto requestDto = new CreateProductRequestDto("asdasdasdasdasdasdasdasdasdasd", 123L, "aasdfgh");
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        client.put()
                                .uri(url)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    void 없는_상품을_수정하면_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/999";
        CreateProductRequestDto requestDto = new CreateProductRequestDto("asd", 123L, "aasdfgh");
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.put()
                                .uri(url)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    void 상품에_이름을_카카오로_생성_후_조회할_수_없다() {
        String url = "http://localhost:" + port + "/api/products";
        CreateProductRequestDto requestDto = new CreateProductRequestDto("카카카오", 123L, "aasdfgh");
        ResponseEntity<Product> response = client.post()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String searchUrl = "http://localhost:" + port + "/api/products/5";
        assertThatExceptionOfType(HttpClientErrorException.Forbidden.class)
                .isThrownBy(() ->
                        client.get()
                                .uri(searchUrl)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    void 상품에_이름을_카카오로_수정후_조회할_수_없다() {
        String url = "http://localhost:" + port + "/api/products/3";
        CreateProductRequestDto requestDto = new CreateProductRequestDto("카카카오", 123L, "aasdfgh");
        ResponseEntity<Product> response = client.put()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        String searchUrl = "http://localhost:" + port + "/api/products/2";
        assertThatExceptionOfType(HttpClientErrorException.Forbidden.class)
                .isThrownBy(() ->
                        client.get()
                                .uri(searchUrl)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    void MD에_의해_권한을_수정하면_조회가_가능하다() {
        String url = "http://localhost:" + port + "/api/products/3";
        ResponseEntity<Void> response = client.patch()
                .uri(url)
                .retrieve()
                .toEntity(Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        String searchUrl = "http://localhost:" + port + "/api/products/3";
        ResponseEntity<Product> getResponse = client.get()
                .uri(searchUrl)
                .retrieve()
                .toEntity(Product.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 상품을_정상적으로_수정하면_200가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/3";
        CreateProductRequestDto requestDto = new CreateProductRequestDto("asd", 123L, "aasdfgh");
        ResponseEntity<Product> response = client.put()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 상품을_정상적으로_삭제하면_204가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/3";
        ResponseEntity<Product> response = client.delete()
                .uri(url)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void 상품을_삭제_실패하면_204가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/999";

        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.delete()
                                .uri(url)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }
}

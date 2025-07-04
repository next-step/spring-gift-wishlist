package gift;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();


    @Test
    void 상품생성시_정상입력되면_200이_반환된다(){
        String url = "http://localhost:" + port + "/api/products";
        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName("상품명&+");
        productRequestDto.setPrice(1000L);
        productRequestDto.setImageUrl("image.jpg");

        var response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productRequestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);
        assert(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    void 상품생성시_협의된상품에_카카오가들어가면_200이_반환된다(){
        String url = "http://localhost:" + port + "/api/products";
        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName("상품명은카카오");
        productRequestDto.setPrice(1000L);
        productRequestDto.setImageUrl("image.jpg");
        productRequestDto.setKakaoWordAllow(true);

        var response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productRequestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);
        assert(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    void 상품생성시_이름을_입력하지않으면_400이_반환된다(){
        String url = "http://localhost:" + port + "/api/products";
        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName("");
        productRequestDto.setPrice(1000L);
        productRequestDto.setImageUrl("image.jpg");

        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(
                        () -> client.post()
                                .uri(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(productRequestDto)
                                .retrieve()
                                .toEntity(ProductResponseDto.class)

                );
    }

    @Test
    void 상품생성시_협의하지않은상품에_카카오가들어가면_400이_반환된다(){
        String url = "http://localhost:" + port + "/api/products";
        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName("상품명은카카오");
        productRequestDto.setPrice(1000L);
        productRequestDto.setImageUrl("image.jpg");

        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(
                        () -> client.post()
                                .uri(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(productRequestDto)
                                .retrieve()
                                .toEntity(ProductResponseDto.class)

                );
    }

    @Test
    void 상품생성시_허용되지않은_특수문자가들어가면_400이_반환된다(){
        String url = "http://localhost:" + port + "/api/products";
        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName("상품명은허용되지않은?");
        productRequestDto.setPrice(1000L);
        productRequestDto.setImageUrl("image.jpg");

        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(
                        () -> client.post()
                                .uri(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(productRequestDto)
                                .retrieve()
                                .toEntity(ProductResponseDto.class)

                );
    }

    @Test
    void 상품생성시_가격에_음수가들어가면_400이_반환된다(){
        String url = "http://localhost:" + port + "/api/products";
        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName("상품명은 상품명?");
        productRequestDto.setPrice(-1000L);
        productRequestDto.setImageUrl("image.jpg");

        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(
                        () -> client.post()
                                .uri(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(productRequestDto)
                                .retrieve()
                                .toEntity(ProductResponseDto.class)

                );
    }

}

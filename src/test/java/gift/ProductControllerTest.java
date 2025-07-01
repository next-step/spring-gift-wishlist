package gift;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();



    @Test
    void 상품_전체_조회_테스트() {
        System.out.println("getAll test");
        var url = "http://localhost:" + port + "/api/products";
        var response = client.get()
                .uri(url)
                .retrieve()
                .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }



    @Test
    void 상품_단건_조회_정상_테스트(){
        System.out.println("getProductById test");
        var url = "http://localhost:" + port + "/api/products/1";
        var response = client.get()
                .uri(url)
                .retrieve()
                .toEntity(Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }



    @Test
    void 상품_단건_조회_없는_ID_상품_조회_시_Not_Found_테스트(){
        System.out.println("getProductById test");
        var url = "http://localhost:" + port + "/api/products/5";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () -> client.get()
                                .uri(url)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }



    @Test
    void 상품_추가_정상_테스트(){
        System.out.println("addProduct test");
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("아이스 카페 아메리카노 T");
        requestDto.setPrice(4500);
        requestDto.setImageUrl("https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg");
        var url = "http://localhost:" + port + "/api/products";
        var response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(requestDto.getName());
    }

    @Test
    void 상품_수정_정상_테스트(){
        System.out.println("updateProduct test");
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("아이스 카페 아메리카노 T");
        requestDto.setPrice(5000);
        requestDto.setImageUrl("https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg");
        var url = "http://localhost:" + port + "/api/products/1";
        var response = client.put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(requestDto.getName());
    }
}

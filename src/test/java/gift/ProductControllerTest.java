package gift;

import static org.assertj.core.api.Assertions.*;

import gift.dto.ProductRequestDto;
import gift.entity.Product;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    private RestClient restClient = RestClient.builder().build();

    @Test
    void 상품_정상_조회(){
        var url = "http://localhost:" + port + "/api/products/1";
        var response = restClient.get()
                .uri(url)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("맥북 에어");
    }

    @Test
    void 없는_상품을_조회하는_경우_404반환(){
        var url = "http://localhost:" + port + "/api/products/115";
        Assertions.assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () ->
                                restClient.get()
                                        .uri(url)
                                        .retrieve()
                                        .toEntity(Void.class)

                );
    }

    @Test
    void 전체_상품을_조회하는_기능(){
        var url = "http://localhost:" + port + "/api/products";
        var response = restClient.get()
                .uri(url)
                .retrieve()
                .toEntity(List.class);
        assertThat(response.getBody().size()).isEqualTo(3);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 상품을_수정하는_기능(){
        var url = "http://localhost:" + port + "/api/products/1";
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("아이폰15");
        requestDto.setPrice(550000);
        requestDto.setImageUrl("https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcRoaNDLkmOrSKI4HJ80_6OrpwF7UAyAme0pw_IO2W4G0JqiQOaHohKg4x48ulWc1py_2VfEVKUw");
        var response = restClient.put()
                .uri(url)
                .body(requestDto)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getBody().getName()).isEqualTo("아이폰15");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    //new ProductRequestDto("아이폰15", 550000, "


}

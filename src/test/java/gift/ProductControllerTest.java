package gift;

import gift.dto.request.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    private RestClient.Builder builder;

    private RestClient client;

    @BeforeEach
    void setUp() {
        this.client = builder.defaultStatusHandler(response -> {
                                return response.getStatusCode().is4xxClientError();
                             })
                             .build();
    }

    @Test
    void 상품명_15자_초과시_400이_반환된다() throws Exception{
        String url = "http://localhost:" + port + "/api/products";

        ProductRequest request = new ProductRequest(
                " 0123456789카카오!!~",
                -2000,
                ""
        );

        ResponseEntity<String> response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 허용되지않은_특수문자_입력시_400이_반환된다() throws Exception{
        String url = "http://localhost:" + port + "/api/products";

        ProductRequest request = new ProductRequest(
                "9abcdef!!!",
                1000,
                "http://"
        );

        ResponseEntity<String> response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void 상품명에_카카오_포함시_400이_반환된다() throws Exception{
        String url = "http://localhost:" + port + "/api/products";

        ProductRequest request = new ProductRequest(
                "카카오97 초콜릿",
                1000,
                "http://"
        );

        ResponseEntity<String> response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}

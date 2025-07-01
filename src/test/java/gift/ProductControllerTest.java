package gift;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestClient;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
}

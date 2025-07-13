package gift.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();

    @Test
    @DisplayName("아이디로 조회가 되는지를 테스트")
    void findByIdTest() {
        var url = "http://localhost:" + port + "/api/products/1";

        var response = client.get().uri(url).retrieve().toEntity(Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var actual = response.getBody();
        assertThat(actual.getName()).isEqualTo("hehe");
    }

    @Test
    @DisplayName("// 존재하지 않는 아이디로 조회 시 404 반환")
    void notFoundHandlerTest() {
        var url = "http://localhost:" + port + "/api/products/3";

        Assertions.assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() -> client.get().uri(url).retrieve().toEntity(Void.class));
    }
}

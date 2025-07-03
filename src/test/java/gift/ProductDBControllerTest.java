package gift;

import gift.Entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
public class ProductDBControllerTest {
    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();

    @Test
    public void test1() {
        var url = "http://localhost:" + port + "/products/1";
        var response = client.get()
                .uri(url)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var actual = response.getBody();
        assertThat(actual.getName()).isEqualTo("아이스 카페 아메리카노 T");
    }

    /*
    // 없는 데이터를 조회 시 예외 처리를 시도했으나 잘 안됨....


    @Test
    public void notFoundId() {
        var url = "http://localhost:" + port + "/products/2";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                    () ->
                            client.get()
                                    .uri(url)
                                    .retrieve()
                                    .toEntity(Void.class)
                );
    }

     */
}
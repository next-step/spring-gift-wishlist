package gift;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {

    @LocalServerPort
    private int port;

    private final static String BASE_URL = "/api/members";

    private RestClient client;

    @BeforeEach
    void Setup() { this.client = RestClient.builder().baseUrl("http://localhost:" + port).build(); }

}

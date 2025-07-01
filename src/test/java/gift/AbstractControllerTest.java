package gift;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractControllerTest {
    @LocalServerPort
    protected int port;

    protected RestClient restClient = RestClient.builder().build();

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }
}

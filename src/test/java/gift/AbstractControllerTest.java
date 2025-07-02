package gift;

import gift.dto.ErrorMessageResponse;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractControllerTest {
    @LocalServerPort
    protected int port;

    protected RestClient restClient = RestClient.builder().build();

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }

    protected void checkErrorResponse(HttpClientErrorException e, int expectedStatus) {
        assertThat(e.getStatusCode().value()).isEqualTo(expectedStatus);
        ErrorMessageResponse errorResponse = e.getResponseBodyAs(ErrorMessageResponse.class);
        assertThat(errorResponse).isNotNull();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(expectedStatus, errorResponse.status(), "상태 코드는 " + expectedStatus + "이어야 합니다.");
    }

}

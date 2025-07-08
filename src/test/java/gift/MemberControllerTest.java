package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("사용자 기능 Test")
public class MemberControllerTest {

  private final int port = 8080;
  private final RestClient client = RestClient.builder().build();

  @Test
  @DisplayName("[1] 정상 회원 가입 Test")
  @Order(1)
  void ValidtestRegister() {
    var url = "http://localhost:" + port + "/api/members/register";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    String formData = "email=abc123@gmail.com&password=123";

    ResponseEntity<String> response = client.post()
        .uri(url)
        .headers(h -> h.addAll(headers))
        .body(formData)
        .retrieve()
        .toEntity(String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND); // 302 redirect
  }

  @Test
  @DisplayName("[2] 이미 존재하는 email 회원 가입 Test")
  @Order(2)
  void inValidRegister() {
    var url = "http://localhost:" + port + "/api/members/register";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    String formData = "email=abc123@gmail.com&password=123";

    try {
      client.post()
          .uri(url)
          .headers(h -> h.addAll(headers))
          .body(formData)
          .retrieve()
          .toEntity(String.class);

      fail("예외가 발생해야 합니다 (중복 이메일)");

    } catch (RestClientResponseException ex) {
      assertThat(ex.getRawStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
      assertThat(ex.getResponseBodyAsString()).contains("이미 존재하는 이메일");
    }
  }
}

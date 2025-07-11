package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WishlistControllerTest {

  private final int port = 8080;

  RestClient restClient;

  @BeforeEach
  void setUp() {
    this.restClient = RestClient.builder()
        .baseUrl("http://localhost:" + port)
        .build();
  }

  private String getAuthToken(String email, String password) {
    String formData = "email=" + email + "&password=" + password;

    ResponseEntity<Void> response = restClient.post()
        .uri("/api/members/login")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .body(formData)
        .retrieve()
        .toBodilessEntity();

    return response.getHeaders()
        .getFirst(HttpHeaders.AUTHORIZATION)
        .replace("Bearer ", "");
  }

  @Test
  @Order(1)
  @DisplayName("[1] 위시리스트 상품 정상 추가")
  void testWishlistAddSuccess() {
    // 회원가입
    String formData = "email=test999@gmail.com&password=Qwer1234!";
    restClient.post()
        .uri("/api/members/register")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .body(formData)
        .retrieve()
        .toBodilessEntity();

    // 로그인 후 토큰 확보
    String token = getAuthToken("test999@gmail.com", "Qwer1234!");

    // 찜 추가 요청
    ResponseEntity<Void> response = restClient.post()
        .uri("/api/products/1/wishlist")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .retrieve()
        .toBodilessEntity();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND); // redirect
  }

  @Test
  @Order(2)
  @DisplayName("[2] 중복 찜시 에러 반환")
  void testWishlistDuplicateAdd() {
    String token = getAuthToken("test999@gmail.com", "Qwer1234!");

    try {
      restClient.post()
          .uri("/api/products/1/wishlist")
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
          .retrieve()
          .toBodilessEntity();

      // 예외가 발생하지 않으면 실패로 간주
      fail("중복 찜 요청에 대해 예외가 발생해야 합니다.");
    } catch (RestClientResponseException e) {
      // 400 ~ 499 범위의 상태 코드가 올 것으로 예상
      assertThat(e.getStatusCode().is4xxClientError()).isTrue();
      assertThat(e.getRawStatusCode()).isEqualTo(404);
      assertThat(e.getResponseBodyAsString()).contains("이미 찜한 상품입니다"); // 예외 메시지 검증
    }
  }

  @Test
  @Order(3)
  @DisplayName("[3] 찜 수량을 양수로 수정하면 성공")
  void testUpdateQuantitySuccess() {
    String token = getAuthToken("test999@gmail.com", "Qwer1234!");
    int newQuantity = 72;

    ResponseEntity<String> response = restClient.post()
        .uri("/api/wishlist/1/quantity")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .body("quantity=" + newQuantity)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .retrieve()
        .toEntity(String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND); // redirect 응답
  }

  @Test
  @Order(4)
  @DisplayName("[4] 찜 수량을 0 이하로 수정하면 실패")
  void testUpdateQuantityFailure() {
    String token = getAuthToken("test999@gmail.com", "Qwer1234!");
    int invalidQuantity = 0;

    try {
      restClient.post()
          .uri("/api/wishlist/1/quantity")
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
          .body("quantity=" + invalidQuantity)
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .retrieve()
          .toBodilessEntity(); // 400이면 예외 발생

      fail("0 이하 수량으로 수정했을 때 예외가 발생해야 합니다.");
    } catch (RestClientResponseException e) {
      assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
      assertThat(e.getResponseBodyAsString()).contains("수량은 1 이상이어야 합니다"); // 예외 메시지 맞게 수정
    }
  }

  @Test
  @Order(5)
  @DisplayName("[5] 찜 상품 삭제 성공 시 리다이렉트")
  void testDeleteWishlistItem_Success() {
    String token = getAuthToken("test999@gmail.com", "Qwer1234!");

    // 상품 1번은 이미 찜되어 있다고 가정
    ResponseEntity<String> response = restClient.post()
        .uri("/api/wishlist/1/delete")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .retrieve()
        .toEntity(String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
  }

  @Test
  @Order(6)
  @DisplayName("[6] 존재하지 않는 찜 상품 삭제 시 404 Not Found")
  void testDeleteWishlistItem_NotFound() {
    String token = getAuthToken("test999@gmail.com", "Qwer1234!");

    try {
      restClient.post()
          .uri("/api/wishlist/999/delete") // 존재하지 않는 상품 ID
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .retrieve()
          .toBodilessEntity();

      fail("존재하지 않는 찜 상품 삭제 시 예외가 발생해야 합니다.");
    } catch (RestClientResponseException e) {
      assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
      assertThat(e.getResponseBodyAsString()).contains("삭제할 찜 항목이 존재하지 않습니다"); // 서비스 예외 메시지 확인
    }
  }
}

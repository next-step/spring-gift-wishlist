package gift;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.dto.ProductDto;
import gift.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@DisplayName("Product 삽입 Test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductRestControllerTest {

  @LocalServerPort
  private int port = 8080;

  private RestClient client = RestClient.builder().build();
  @Test
  @DisplayName("1번 Product 삽입 Test")
  void test1() {
    var url = "http://localhost:" + port + "/api/products/1";
    var response = client.get()
        .uri(url)
        .retrieve()
        .toEntity(Product.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    var actual = response.getBody();
    assertThat(actual.getName()).isEqualTo("아이스 카페 아메리카노 T");
    assertThat(actual.getPrice()).isEqualTo(600);
    assertThat(actual.getImageUrl()).isEqualTo(
        "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg");
  }

  @Test
  @DisplayName("2번 Product 삽입 Test")
  void test2() {
    var url = "http://localhost:" + port + "/api/products/2";
    var response = client.get()
        .uri(url)
        .retrieve()
        .toEntity(Product.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    var actual = response.getBody();
    assertThat(actual.getName()).isEqualTo("코코아 파우더");
    assertThat(actual.getPrice()).isEqualTo(7600);
    assertThat(actual.getImageUrl()).isEqualTo(
        "https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcRWDlZJBZiXDT8JlPp8iS5eeyJDdsISVl9lBSTXgcLhUAjCjU-qttjbpgg8-JPRHMWWOKEuGqlQWTqIvg_Hw_dAaEdZYPM-JF5z7KMOGPGkpGIj3z9WZMJG");
  }

  @Test
  @DisplayName("상품 이름 길이 초과 시 오류 메시지가 나타나는지 확인")
  void overLengthTest() {
    // 잘못된 상품 이름 데이터
    String invalidFormData = "name=이름이15자를초과하는상품명이랍니다&price=1500";

    var response = client.post()
        .uri("http://localhost:" + port + "/admin/products")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(invalidFormData)
        .retrieve()
        .toEntity(String.class); // => HTML 텍스트 그대로 받아서 확인

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    String html = response.getBody();

    // HTML 내 오류 메시지 확인 (기본 메시지 혹은 커스텀 메시지)
    assertThat(html).contains("상품 이름은 최대 15자까지 입력가능 합니다");
  }

  @Test
  @DisplayName("허가되지 않은 특수문자 필터링 확인")
  void invalidHyperTextTest() {
    // 잘못된 상품 이름 데이터
    String invalidFormData = "name=**&price=1500";

    var response = client.post()
        .uri("http://localhost:" + port + "/admin/products")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(invalidFormData)
        .retrieve()
        .toEntity(String.class); // => HTML 텍스트 그대로 받아서 확인

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    String html = response.getBody();

    // HTML 내 오류 메시지 확인 (기본 메시지 혹은 커스텀 메시지)
    assertThat(html).contains("상품 이름에는 (), [], +, -, &amp;, /, _ 외의 특수문자는 사용 불가합니다");
  }
}

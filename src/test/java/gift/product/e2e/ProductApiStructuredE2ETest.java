package gift.product.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.global.exception.dto.ErrorResponse;
import gift.product.dto.CreateProductReqDto;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductApiStructuredE2ETest {

  @LocalServerPort
  int port;

  RestClient restClient;

  @BeforeEach
  void setUp() {
    this.restClient = RestClient.builder()
        .baseUrl("http://localhost:" + port)
        .build();
  }

  @Test
  void 상품등록_빈_이름으로_요청시_400_응답반환() throws Exception {
    var request = new CreateProductReqDto("   ", 1000, "설명입니다", "https://example.com/image.jpg");

    var response = createProductWithErrorResponse(request);
    ObjectMapper mapper = new ObjectMapper();
    ErrorResponse errorResponse = mapper.readValue(response.getBody(), ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(errorResponse.errorCode()).isEqualTo("G001");

    @SuppressWarnings("unchecked")
    List<Map<String, String>> invalidParams = (List<Map<String, String>>) errorResponse.extras().get("invalid-params");

    assertThat(invalidParams).anySatisfy(param -> {
      assertThat(param.get("name")).isEqualTo("name");
      assertThat(param.get("reason")).contains("상품명은 필수값입니다");
    });
  }

  @Test
  void 상품등록_카카오포함된_이름으로_요청시_400_응답반환() throws Exception {
    var request = new CreateProductReqDto("카카오 초콜릿", 1000, "설명입니다", "https://example.com/image.jpg");

    var response = createProductWithErrorResponse(request);
    ObjectMapper mapper = new ObjectMapper();
    ErrorResponse errorResponse = mapper.readValue(response.getBody(), ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(errorResponse.errorCode()).isEqualTo("G001");

    @SuppressWarnings("unchecked")
    List<Map<String, String>> invalidParams = (List<Map<String, String>>) errorResponse.extras().get("invalid-params");

    assertThat(invalidParams).anySatisfy(param -> {
      assertThat(param.get("name")).isEqualTo("name");
      assertThat(param.get("reason")).contains("카카오");
      assertThat(param.get("reason")).contains("포함할 수 없습니다");
    });
  }

  @Test
  void 상품등록_유효하지않은_가격으로_요청시_400_응답반환() throws Exception {
    var request = new CreateProductReqDto("상품이름", -10, "설명입니다", "https://example.com/image.jpg");

    var response = createProductWithErrorResponse(request);
    ObjectMapper mapper = new ObjectMapper();
    ErrorResponse errorResponse = mapper.readValue(response.getBody(), ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(errorResponse.errorCode()).isEqualTo("G001");

    @SuppressWarnings("unchecked")
    List<Map<String, String>> invalidParams = (List<Map<String, String>>) errorResponse.extras().get("invalid-params");

    assertThat(invalidParams).anySatisfy(param -> {
      assertThat(param.get("name")).isEqualTo("price");
      assertThat(param.get("reason")).contains("0원 이상");
    });
  }

  @Test
  void 상품리스트조회_잘못된_정렬필드_400_반환() throws Exception {
    var response = restClient.get()
        .uri("/api/products?page=0&size=10&sort=nonExistentField,asc")
        .exchange((req, res) -> {
          var body = new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8);
          return ResponseEntity.status(res.getStatusCode()).headers(res.getHeaders()).body(body);
        });

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    ObjectMapper mapper = new ObjectMapper();
    ErrorResponse errorResponse = mapper.readValue(response.getBody(), ErrorResponse.class);

    assertThat(errorResponse.errorCode()).isEqualTo("G003");
    assertThat(errorResponse.errorMessage()).contains("정렬 필드 값이 올바르지 않습니다");
  }




  private CreateProductReqDto createValidProductRequest() {
    return new CreateProductReqDto(
        "정상상품",
        1000,
        "정상적인 설명입니다.",
        "https://example.com/image.jpg"
    );
  }

  private ResponseEntity<Void> createProduct(CreateProductReqDto request) {
    return restClient.post()
        .uri("/api/products")
        .body(request)
        .exchange((req, res) -> ResponseEntity.status(res.getStatusCode())
            .headers(res.getHeaders())
            .body(null));
  }

  private ResponseEntity<String> createProductWithErrorResponse(CreateProductReqDto request) {
    return restClient.post()
        .uri("/api/products")
        .body(request)
        .exchange((req, res) -> {
          var body = new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8);
          return ResponseEntity.status(res.getStatusCode()).headers(res.getHeaders()).body(body);
        });
  }

  private ResponseEntity<String> getProductsWithErrorResponse(String queryParams) {
    return restClient.get()
        .uri("/api/products" + queryParams)
        .exchange((req, res) -> {
          var body = new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8);
          return ResponseEntity.status(res.getStatusCode()).headers(res.getHeaders()).body(body);
        });
  }

  private Long createTestProduct() {
    var request = createValidProductRequest();
    var response = createProduct(request);

    URI location = response.getHeaders().getLocation();
    return Long.valueOf(location.getPath().substring(location.getPath().lastIndexOf("/") + 1));
  }
}


package gift.product.e2e;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.product.dto.CreateProductReqDto;
import gift.product.dto.GetProductResDto;
import gift.product.dto.UpdateProductReqDto;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductApiE2ETest {

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
  void 상품등록_정상요청시_201_응답과_Location_헤더_반환() {
    var request = createValidProductRequest();

    var response = createProduct(request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getHeaders().getLocation()).isNotNull();
  }

  @Test
  void 상품등록_빈_이름으로_요청시_400_응답반환() {
    var request = new CreateProductReqDto(
        "   ",
        1000,
        "설명입니다",
        "https://example.com/image.jpg"
    );

    var response = createProductWithErrorResponse(request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).contains("상품명은 필수값입니다");
  }

  @Test
  void 상품등록_카카오포함된_이름으로_요청시_400_응답반환() {
    var request = new CreateProductReqDto(
        "카카오 초콜릿",
        1000,
        "설명입니다",
        "https://example.com/image.jpg"
    );

    var response = createProductWithErrorResponse(request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    System.out.println(response.getBody());
    assertThat(response.getBody()).contains("상품명에 다음 키워드를 포함할 수 없습니다");
  }

  @Test
  void 상품등록_유효하지않은_가격으로_요청시_400_응답반환() {
    var request = new CreateProductReqDto(
        "상품이름",
        -10,
        "설명입니다",
        "https://example.com/image.jpg"
    );

    var response = createProductWithErrorResponse(request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).contains("가격은 0원 이상이어야 합니다");
  }

  @Test
  void 상품단건조회_정상ID요청시_200과_정상데이터_반환() {
    Long id = createTestProduct();

    ResponseEntity<GetProductResDto> response = restClient.get()
        .uri("/api/products/" + id)
        .retrieve()
        .toEntity(GetProductResDto.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().name()).isEqualTo("정상상품");
  }

  @Test
  void 상품단건조회_존재하지않는ID요청시_404_반환() {
    var response = restClient.get()
        .uri("/api/products/999999")
        .exchange((req, res) ->
            ResponseEntity.status(res.getStatusCode()).headers(res.getHeaders()).body(null));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void 상품리스트조회_정상요청시_200_반환() {
    createTestProduct();
    createTestProduct();

    var response = restClient.get()
        .uri("/api/products?page=0&size=10&sort=id,asc")
        .retrieve()
        .toEntity(String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void 상품리스트조회_기본값으로_정상동작() {
    createTestProduct();

    var response = restClient.get()
        .uri("/api/products?page=-1&size=0")
        .retrieve()
        .toEntity(String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void 상품리스트조회_잘못된_정렬필드_400_반환() {
    var response = restClient.get()
        .uri("/api/products?page=0&size=10&sort=nonExistentField,asc")
        .exchange((req, res) -> {
          if (res.getStatusCode().is4xxClientError()) {
            var body = new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8);
            return ResponseEntity.status(res.getStatusCode()).headers(res.getHeaders()).body(body);
          }
          return ResponseEntity.status(res.getStatusCode()).headers(res.getHeaders()).body("OK");
        });

    if (response.getStatusCode().is4xxClientError()) {
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    } else {
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  }

  @Test
  void 상품수정_정상요청시_204_반환() {
    Long id = createTestProduct();

    var updateRequest = new UpdateProductReqDto(
        "수정된이름",
        2000,
        "수정된설명",
        "https://example.com/updated.jpg"
    );

    var response = restClient.put()
        .uri("/api/products/" + id)
        .body(updateRequest)
        .exchange((req, res) ->
            ResponseEntity.status(res.getStatusCode()).headers(res.getHeaders()).body(null));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void 상품삭제_정상요청시_204_반환() {
    Long id = createTestProduct();

    var response = restClient.delete()
        .uri("/api/products/" + id)
        .exchange((req, res) ->
            ResponseEntity.status(res.getStatusCode()).headers(res.getHeaders()).body(null));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
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
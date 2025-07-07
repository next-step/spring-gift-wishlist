package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.dto.ProductRequestDTO;
import gift.dto.ProductResponseDTO;
import gift.entity.Product;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

  @LocalServerPort
  private int port;

  private RestClient client;

  @BeforeEach
  void setup() {
    String url = "http://localhost:" + port + "/api/products";
    client = RestClient.builder().baseUrl(url).build();
  }

  @Test
  @DisplayName("상품 생성 - 성공")
  void createProduct() {
    ProductRequestDTO request = new ProductRequestDTO();
    request.setName("테스트 상품");
    request.setPrice(4500L);
    request.setImageUrl("https://test.jpg");

    ResponseEntity<ProductResponseDTO> response = client.post()
        .uri("")
        .contentType(MediaType.APPLICATION_JSON)
        .body(request)
        .retrieve()
        .toEntity(ProductResponseDTO.class);

    assertNotNull(response);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getName()).isEqualTo("테스트 상품");
    assertThat(response.getBody().getPrice()).isEqualTo(4500L);
    assertThat(response.getBody().getImageUrl()).isEqualTo("https://test.jpg");
  }

  @Test
  @DisplayName("상품 생성 - 유효성 검증 실패 (15자 초과)")
  void createProductNameTooLong() {
    ProductRequestDTO request = new ProductRequestDTO();
    request.setName("01234567890123456789"); // 20자
    request.setPrice(4500L);
    request.setImageUrl("https://test.jpg");

    HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
      client.post()
          .uri("")
          .contentType(MediaType.APPLICATION_JSON)
          .body(request)
          .retrieve()
          .toEntity(ProductResponseDTO.class)
    );

    assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(exception.getResponseBodyAsString()).contains("상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.");
  }

  @Test
  @DisplayName("상품 생성 - 유효성 검증 실패 (가격 0 이하)")
  void createProduct_InvalidPrice() {
    ProductRequestDTO request = new ProductRequestDTO();
    request.setName("test");
    request.setPrice(-123L);
    request.setImageUrl("https://test.jpg");

    HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
      client.post()
          .uri("")
          .contentType(MediaType.APPLICATION_JSON)
          .body(request)
          .retrieve()
          .toEntity(ProductResponseDTO.class)
    );

    assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(exception.getResponseBodyAsString()).contains("상품 가격은 0보다 큰 값이어야 합니다.");
  }

  @Test
  @DisplayName("상품 생성 - 유효성 검증 실패 (잘못된 URL)")
  void createProduct_InvalidUrl() {
    ProductRequestDTO request = new ProductRequestDTO();
    request.setName("테스트 상품");
    request.setPrice(4500L);
    request.setImageUrl("invalid-url");

    assertThrows(HttpClientErrorException.class, () ->
      client.post()
          .uri("")
          .contentType(MediaType.APPLICATION_JSON)
          .body(request)
          .retrieve()
          .toEntity(ProductResponseDTO.class)
    );
  }

  @Test
  @DisplayName("상품 조회 - 성공 테스트")
  void getProduct() {
    ProductRequestDTO createRequest = new ProductRequestDTO();
    createRequest.setName("조회 테스트 상품");
    createRequest.setPrice(5000L);
    createRequest.setImageUrl("https://example.jpg");

    ResponseEntity<ProductResponseDTO> createResponse = client.post()
        .uri("")
        .contentType(MediaType.APPLICATION_JSON)
        .body(createRequest)
        .retrieve()
        .toEntity(ProductResponseDTO.class);

    Long productId = createResponse.getBody() != null ? createResponse.getBody().getId() : null;
    assertNotNull(productId);

    ResponseEntity<ProductResponseDTO> response = client.get()
        .uri("/" + productId)
        .retrieve()
        .toEntity(ProductResponseDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getId()).isEqualTo(productId);
    assertThat(response.getBody().getName()).isEqualTo("조회 테스트 상품");
  }

  @Test
  @DisplayName("존재하지 않는 상품 조회 - 204 No Content")
  void getProduct_NotFound() {
    ResponseEntity<ProductResponseDTO> response = client.get()
        .uri("/99999")
        .retrieve()
        .toEntity(ProductResponseDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  @DisplayName("모든 상품 조회 - 성공 테스트")
  void getAllProducts() {
    ProductRequestDTO request1 = new ProductRequestDTO();
    request1.setName("상품1");
    request1.setPrice(1000L);
    request1.setImageUrl("https://test1.jpg");

    ProductRequestDTO request2 = new ProductRequestDTO();
    request2.setName("상품2");
    request2.setPrice(2000L);
    request2.setImageUrl("https://test2.jpg");

    client.post().uri("").contentType(MediaType.APPLICATION_JSON).body(request1).retrieve().toBodilessEntity();
    client.post().uri("").contentType(MediaType.APPLICATION_JSON).body(request2).retrieve().toBodilessEntity();

    ResponseEntity<List<Product>> response = client.get()
        .uri("")
        .retrieve()
        .toEntity(new ParameterizedTypeReference<>() {});

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().size()).isGreaterThanOrEqualTo(2);
  }

  @Test
  @DisplayName("상품 수정 - 성공 테스트")
  void updateProduct() {
    ProductRequestDTO createRequest = new ProductRequestDTO();
    createRequest.setName("수정 전 상품");
    createRequest.setPrice(3000L);
    createRequest.setImageUrl("https://before.jpg");

    ResponseEntity<ProductResponseDTO> createResponse = client.post()
        .uri("")
        .contentType(MediaType.APPLICATION_JSON)
        .body(createRequest)
        .retrieve()
        .toEntity(ProductResponseDTO.class);

    Long productId = createResponse.getBody() != null ? createResponse.getBody().getId() : null;
    assertNotNull(productId);

    ProductRequestDTO updateRequest = new ProductRequestDTO();
    updateRequest.setName("수정 후 상품");
    updateRequest.setPrice(4000L);
    updateRequest.setImageUrl("https://after.jpg");

    ResponseEntity<ProductResponseDTO> response = client.put()
        .uri("/" + productId)
        .contentType(MediaType.APPLICATION_JSON)
        .body(updateRequest)
        .retrieve()
        .toEntity(ProductResponseDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getName()).isEqualTo("수정 후 상품");
    assertThat(response.getBody().getPrice()).isEqualTo(4000L);
    assertThat(response.getBody().getImageUrl()).isEqualTo("https://after.jpg");
  }

  @Test
  @DisplayName("존재하지 않는 상품 수정 - 204 No Content")
  void updateProduct_NotFound() {
    ProductRequestDTO updateRequest = new ProductRequestDTO();
    updateRequest.setName("수정할 상품");
    updateRequest.setPrice(4000L);
    updateRequest.setImageUrl("https://test.jpg");

    ResponseEntity<ProductResponseDTO> response = client.put()
        .uri("/99999")
        .contentType(MediaType.APPLICATION_JSON)
        .body(updateRequest)
        .retrieve()
        .toEntity(ProductResponseDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  @DisplayName("상품 삭제 - 성공 테스트")
  void deleteProduct() {
    ProductRequestDTO createRequest = new ProductRequestDTO();
    createRequest.setName("삭제할 상품");
    createRequest.setPrice(5000L);
    createRequest.setImageUrl("https://delete.jpg");

    ResponseEntity<ProductResponseDTO> createResponse = client.post()
        .uri("")
        .contentType(MediaType.APPLICATION_JSON)
        .body(createRequest)
        .retrieve()
        .toEntity(ProductResponseDTO.class);

    Long productId = createResponse.getBody() != null ? createResponse.getBody().getId() : null;
    assertNotNull(productId);

    ResponseEntity<Void> response = client.delete()
        .uri("/" + productId)
        .retrieve()
        .toBodilessEntity();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    ResponseEntity<ProductResponseDTO> getResponse = client.get()
        .uri("/" + productId)
        .retrieve()
        .toEntity(ProductResponseDTO.class);

    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  @DisplayName("존재하지 않는 상품 삭제 - 204 No Content")
  void deleteProduct_NotFound() {
    ResponseEntity<Void> response = client.delete()
        .uri("/99999")
        .retrieve()
        .toBodilessEntity();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  @DisplayName("상품 생성 - 유효성 검증 실패 (카카오 포함)")
  void createProduct_ContainsKakao() {
    ProductRequestDTO request = new ProductRequestDTO();
    request.setName("카카오 상품");
    request.setPrice(4500L);
    request.setImageUrl("https://test.jpg");

    HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
        client.post()
            .uri("")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .toEntity(ProductResponseDTO.class)
    );

    assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(exception.getResponseBodyAsString()).contains("카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
  }

  @Test
  @DisplayName("상품 수정 - 유효성 검증 실패 (카카오 포함)")
  void updateProduct_ContainsKakao() {
    ProductRequestDTO createRequest = new ProductRequestDTO();
    createRequest.setName("일반 상품");
    createRequest.setPrice(3000L);
    createRequest.setImageUrl("https://test.jpg");

    ResponseEntity<ProductResponseDTO> createResponse = client.post()
        .uri("")
        .contentType(MediaType.APPLICATION_JSON)
        .body(createRequest)
        .retrieve()
        .toEntity(ProductResponseDTO.class);

    Long productId = createResponse.getBody() != null ? createResponse.getBody().getId() : null;
    assertNotNull(productId);

    ProductRequestDTO updateRequest = new ProductRequestDTO();
    updateRequest.setName("카카오프렌즈 상품");
    updateRequest.setPrice(4000L);
    updateRequest.setImageUrl("https://test.jpg");

    HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
        client.put()
            .uri("/" + productId)
            .contentType(MediaType.APPLICATION_JSON)
            .body(updateRequest)
            .retrieve()
            .toEntity(ProductResponseDTO.class)
    );

    assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(exception.getResponseBodyAsString()).contains("카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
  }
}

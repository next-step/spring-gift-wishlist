package gift;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

  @LocalServerPort
  private int port;

  private RestClient restClient;

  @BeforeEach
  void setUp(){
    restClient= RestClient.builder().build();
    //빌더 패턴 사용.
  }

  @Test
  void 상품조회() {
    String url="http://localhost:"+port+"/api/products/1";
    ProductResponseDto responseDto = restClient.get().uri(url).retrieve().body(ProductResponseDto.class);
    assertThat(responseDto.getId()).isEqualTo(1L);
    assertThat(responseDto.getName()).isEqualTo("물통");
    assertThat(responseDto.getPrice()).isEqualTo(1000L);
    assertThat(responseDto.getImageUrl()).isEqualTo("https://www.naver.com");
  }

  @Test
  void 존재하지_않는_아이디로_조회하면_404반환() {
    String url="http://localhost:"+port+"/api/products/2";
    assertThatExceptionOfType(HttpClientErrorException.NotFound.class).isThrownBy(
        ()-> restClient.get().uri(url).retrieve().toEntity(ProductResponseDto.class)
    );

  }

  @Test
  void 상품등록() {
    String url="http://localhost:"+port+"/api/products";
    ProductRequestDto requestDto=new ProductRequestDto("테스트",100L,"http://test");
    ProductResponseDto responseDto = restClient.post().uri(url).contentType(MediaType.APPLICATION_JSON) // JSON 타입 지정
        .body(requestDto).retrieve().body(ProductResponseDto.class);
    assertThat(responseDto.getName()).isEqualTo("테스트");
    assertThat(responseDto.getPrice()).isEqualTo(100L);
    assertThat(responseDto.getImageUrl()).isEqualTo("http://test");
  }

  @Test
  void 상품수정() {
    String url="http://localhost:"+port+"/api/products/1";
    ProductRequestDto requestDto=new ProductRequestDto("수정",100L,"http://patch_test");
    ProductResponseDto responseDto = restClient.patch().uri(url).contentType(MediaType.APPLICATION_JSON) // JSON 타입 지정
        .body(requestDto).retrieve().body(ProductResponseDto.class);
    assertThat(responseDto.getName()).isEqualTo("수정");
    assertThat(responseDto.getPrice()).isEqualTo(100L);
    assertThat(responseDto.getImageUrl()).isEqualTo("http://patch_test");
  }

  @Test
  void 존재하지_않는_아이디로_업데이트시_404반환() {
    String url = "http://localhost:" + port + "/api/products/2";
    ProductRequestDto requestDto=new ProductRequestDto("수정",100L,"http://patch_test");

    assertThatExceptionOfType(HttpClientErrorException.NotFound.class).isThrownBy(
        () -> restClient.patch().uri(url).contentType(MediaType.APPLICATION_JSON) // JSON 타입 지정
            .body(requestDto).retrieve().body(ProductResponseDto.class)
    );
  }

  @Test
  void 상품삭제() {
    String url="http://localhost:"+port+"/api/products/1";
    ResponseEntity<Void> response = restClient.delete().uri(url).retrieve()
        .toBodilessEntity();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  void 존재하지_않는_아이디로_삭제시_404반환() {
    String url="http://localhost:"+port+"/api/products/2";
    assertThatExceptionOfType(HttpClientErrorException.NotFound.class).isThrownBy(
        ()-> restClient.delete().uri(url).retrieve().toEntity(ProductResponseDto.class)
    );
  }
}

package gift.product.controller;


import gift.domain.Product;
import gift.global.error.ErrorResponse;
import gift.product.dto.ProductCreateRequest;
import gift.product.dto.ProductResponse;
import gift.product.dto.ProductUpdateRequest;
import gift.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductRepository productRepository;

    RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port + "/api/products")
                .build();
    }
    @AfterEach
    void clear() {
        productRepository.deleteAll();
    }


    @Test
    @DisplayName("상품 등록 성공")
    void addProductSuccess() {

        ProductCreateRequest productDto = new ProductCreateRequest("스윙칩", 3000, "data:image/~base64,");

        ResponseEntity<Void> response = restClient.post()
                .body(productDto)
                .retrieve()
                .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("상품 등록 실패 - 상품 이름에 카카오 포함")
    void addProductFailCase1() {
        ProductCreateRequest productDto = new ProductCreateRequest("카카오", 3000, "data:image/~base64,");

        assertThatThrownBy(()->restClient.post()
                .body(productDto)
                .retrieve()
                .body(ErrorResponse.class))
                .isInstanceOf(HttpClientErrorException.BadRequest.class);
    }

    @Test
    @DisplayName("상품 등록 실패 - 상품 가격이 0이하")
    void addProductFailCase2() {
        ProductCreateRequest productDto = new ProductCreateRequest("스윙칩", 0, "data:image/~base64,");

        assertThatThrownBy(()->restClient.post()
                .body(productDto)
                .retrieve()
                .body(ErrorResponse.class))
                .isInstanceOf(HttpClientErrorException.BadRequest.class);
    }

    @Test
    @DisplayName("상품 등록 실패 - url이 빈 칸")
    void addProductFailCase3() {
        ProductCreateRequest productDto = new ProductCreateRequest("스윙칩", 1000, " ");

        assertThatThrownBy(()->restClient.post()
                .body(productDto)
                .retrieve()
                .body(ErrorResponse.class))
                .isInstanceOf(HttpClientErrorException.BadRequest.class);
    }

    @Test
    @DisplayName("상품 조회 성공")
    void getProductSuccess() {
        Product product = addProductCase();

        ResponseEntity<ProductResponse> response = restClient.get()
                .uri("/{id}",product.getId())
                .retrieve()
                .toEntity(ProductResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(product.getId());
        assertThat(response.getBody().getName()).isEqualTo(product.getName());
        assertThat(response.getBody().getPrice()).isEqualTo(product.getPrice());
        assertThat(response.getBody().getImageURL()).isEqualTo(product.getImageURL());
    }

    @Test
    @DisplayName("상품 조회 실패")
    void getProductFail() {
        assertThatThrownBy(()-> {
            restClient.get()
            .uri("/{id}",UUID.randomUUID())
                    .retrieve()
                    .toEntity(ProductResponse.class);
        }).isInstanceOf(HttpClientErrorException.NotFound.class);
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void deleteProductSuccess() {
        Product product = addProductCase();

        ResponseEntity<Void> response = restClient.delete()
                .uri("/{id}",product.getId())
                .retrieve()
                .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("상품 업데이트 성공")
    void updateProductSuccess() {
        Product product = addProductCase();

        ProductUpdateRequest productDto = new ProductUpdateRequest("포카칩", 3000, "data:image/~base64,");
        ResponseEntity<Void> response = restClient.put()
                .uri("/{id}",product.getId())
                .body(productDto)
                .retrieve()
                .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("상품 업데이트 실패 - 존재하는 상품 없음")
    void updateProductFailCase1() {

        ProductUpdateRequest productDto = new ProductUpdateRequest("포카칩", 3000, "data:image/~base64,");
        assertThatThrownBy(()->restClient.put()
                .uri("/{id}",UUID.randomUUID())
                .body(productDto)
                .retrieve()
                .toEntity(Void.class)
        ).isInstanceOf(HttpClientErrorException.NotFound.class);
    }

    @Test
    @DisplayName("상품 업데이트 실패 - validation 통과 실패")
    void updateProductFailCase2() {

        Product product = addProductCase();

        ProductUpdateRequest productDto = new ProductUpdateRequest("카카오", -1, "data:image/~base64,");
        assertThatThrownBy(()->restClient.put()
                .uri("/{id}", product.getId())
                .body(productDto)
                .retrieve()
                .toEntity(Void.class)
        ).isInstanceOf(HttpClientErrorException.BadRequest.class);
    }

    @Test
    @DisplayName("모든 상품 조회")
    void getAllProducts() {
        for (int i=0; i<10; i++) {
            addProductCase();
        }

        ResponseEntity<List> repsonse = restClient.get()
                .retrieve()
                .toEntity(List.class);

        assertThat(repsonse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repsonse.getBody().size()).isEqualTo(10);
    }



    @Test
    void deleteProductFail() {
        assertThatThrownBy(()-> {
            restClient.delete()
                    .uri("/{id}", UUID.randomUUID())
                    .retrieve()
                    .toEntity(ProductResponse.class);
        }).isInstanceOf(HttpClientErrorException.NotFound.class);
    }

    private Product addProductCase() {
        Product product = new Product("스윙칩", 3000, "data:image/~base64,");
        UUID uuid = productRepository.save(product);
        return product;
    }

}
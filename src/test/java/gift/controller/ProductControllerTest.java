package gift.controller;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.dto.CreateMemberRequestDto;
import gift.dto.CreateProductRequestDto;
import gift.dto.DeleteMemberRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Product;
import gift.service.MemberService;
import gift.service.ProductService;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    String token;
    @LocalServerPort
    private int port;
    private RestClient client = RestClient.builder().build();
    @Autowired
    private ProductService productService;
    @Autowired
    private MemberService memberService;

    @Test
    void 전체_조회하면_200이_반환된다() {
        String url = "http://localhost:" + port + "/api/products";
        ResponseEntity<List<ProductResponseDto>> response = client.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ProductResponseDto>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 존재하는_아이디로_개별조회하면_200이_반환된다() {
        String url = "http://localhost:" + port + "/api/products/1";
        ResponseEntity<ProductResponseDto> response = client.get()
                .uri(url)
                .retrieve()
                .toEntity(ProductResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 존재하지_않는_아이디로_개별조회하면_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/999";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.get()
                                .uri(url)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    void 상품등록에_성공하면_201가_반환된다() {
        String url = "http://localhost:" + port + "/api/products";
        CreateProductRequestDto requestDto = new CreateProductRequestDto("asd", 123L, "aasdfgh");
        ResponseEntity<Product> response = client.post()
                .uri(url)
                .header("Authorization", token)
                .body(requestDto)
                .retrieve()
                .toEntity(Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 상품등록에_실패하면_400가_반환된다() {
        String url = "http://localhost:" + port + "/api/products";
        CreateProductRequestDto requestDto = new CreateProductRequestDto(
                "asdasdasdasdasdasdasdasdasdasd", 123L, "aasdfgh");
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        client.post()
                                .uri(url)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    void 상품수정에_실패하면_400가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/1";
        CreateProductRequestDto requestDto = new CreateProductRequestDto(
                "asdasdasdasdasdasdasdasdasdasd", 123L, "aasdfgh");
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(() ->
                        client.put()
                                .uri(url)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    void 없는_상품을_수정하면_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/999";
        CreateProductRequestDto requestDto = new CreateProductRequestDto("asd", 123L, "aasdfgh");
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.put()
                                .uri(url)
                                .header("Authorization", token)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    void 상품을_정상적으로_수정하면_200가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/3";
        CreateProductRequestDto requestDto = new CreateProductRequestDto("asd", 123L, "aasdfgh");
        ResponseEntity<ProductResponseDto> response = client.put()
                .uri(url)
                .header("Authorization", token)
                .body(requestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 상품을_정상적으로_삭제하면_204가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/3";
        ResponseEntity<ProductResponseDto> response = client.delete()
                .uri(url)
                .header("Authorization", token)
                .retrieve()
                .toEntity(ProductResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void 상품을_삭제_실패하면_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/products/999";

        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.delete()
                                .uri(url)
                                .header("Authorization", token)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @AfterEach
    void deleteTestProduct() {
        try {
            productService.deleteProductById(4L);
        } catch (Exception e) {

        }
        try {
            memberService.deleteMember(
                    new DeleteMemberRequestDto("test@asd.asd", "asd"));
        } catch (Exception e) {

        }

    }

    @BeforeEach
    void CreateToken() {
        CreateMemberRequestDto requestDto = new CreateMemberRequestDto("test@asd.asd", "asd");
        token = memberService.createMember(requestDto).token();
    }
}

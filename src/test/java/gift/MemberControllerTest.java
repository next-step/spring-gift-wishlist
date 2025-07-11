package gift;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.dto.ProductResponseDto;
import gift.dto.WishListProductRequestDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberControllerTest {
    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();

    private String testJWTToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmNkQHB1c2FuLmFjLmtyIiwiZW1haWwiOiJhYmNkQHB1c2FuLmFjLmtyIn0.WGDriDkB5paOlUxALdjM4cZqo8ZE2YZ0yN8nwu5VjRk";

    @Test
    @Order(1)
    void 회원가입_잘못된_이메일_입력_테스트(){
        System.out.println("Member Register Not Valid Email test");
        MemberRequestDto requestDto = new MemberRequestDto("qwertypusan.ac.kr", "12345678");
        var url = "http://localhost:" + port + "/api/members/membership";
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(MemberResponseDto.class)
                );
    }

    @Test
    @Order(2)
    void 회원가입_잘못된_비밀번호_입력_테스트(){
        System.out.println("Member Register Not Valid Email test");
        MemberRequestDto requestDto = new MemberRequestDto("abcd@pusan.ac.kr", "1234");
        var url = "http://localhost:" + port + "/api/members/membership";
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(MemberResponseDto.class)
                );
    }

    @Test
    @Order(3)
    void 회원가입_중복_이메일_테스트(){
        System.out.println("Elready Exist Email Register test");
        MemberRequestDto requestDto = new MemberRequestDto("abc@pusan.ac.kr", "12345678");
        var url = "http://localhost:" + port + "/api/members/membership";
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () -> client.post()
                                .uri(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(MemberResponseDto.class)
                );
    }

    @Test
    @Order(4)
    void 회원가입_정상_테스트(){
        System.out.println("Member Register Success test");
        MemberRequestDto requestDto = new MemberRequestDto("zxc@pusan.ac.kr", "12345678");
        var url = "http://localhost:" + port + "/api/members/membership";
        var response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDto)
                .retrieve()
                .toEntity(MemberResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @Order(5)
    void 로그인_잘못된_이메일_입력_테스트(){
        System.out.println("Member Login Not Valid Email test");
        MemberRequestDto requestDto = new MemberRequestDto("qwertypusan.ac.kr", "12345678");
        var url = "http://localhost:" + port + "/api/members/login";
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(MemberResponseDto.class)
                );
    }

    @Test
    @Order(6)
    void 로그인_잘못된_비밀번호_입력_테스트(){
        System.out.println("Member Login Not Valid Email test");
        MemberRequestDto requestDto = new MemberRequestDto("abcd@pusan.ac.kr", "1234");
        var url = "http://localhost:" + port + "/api/members/login";
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(MemberResponseDto.class)
                );
    }

    @Test
    @Order(7)
    void 로그인_없는_이메일_시도_테스트(){
        System.out.println("Member Login Not register Email test");
        MemberRequestDto requestDto = new MemberRequestDto("qwerty@pusan.ac.kr", "12345678");
        var url = "http://localhost:" + port + "/api/members/login";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () ->
                            client.post()
                                    .uri(url)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(requestDto)
                                    .retrieve()
                                    .toEntity(MemberResponseDto.class)
                        );
    }

    @Test
    @Order(8)
    void 로그인_틀린_비밀번호_테스트(){
        System.out.println("Member Login Invalid password test");
        MemberRequestDto requestDto = new MemberRequestDto("abcd@pusan.ac.kr", "12345678888");
        var url = "http://localhost:" + port + "/api/members/login";
        assertThatExceptionOfType(HttpClientErrorException.Forbidden.class)
                .isThrownBy(
                        () -> client.post()
                                .uri(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(MemberResponseDto.class)
                );
    }

    @Test
    @Order(9)
    void 로그인_정상_테스트(){
        System.out.println("Member Login success test");
        MemberRequestDto requestDto = new MemberRequestDto("abcd@pusan.ac.kr", "12345678");
        var url = "http://localhost:" + port + "/api/members/login";
        var response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDto)
                .retrieve()
                .toEntity(MemberResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(10)
    void 위시_리스트_조회_테스트(){
        System.out.println("Get WishList Product test");
        var url = "http://localhost:" + port + "/api/members/wishlist";
        var response = client.get()
                .uri(url)
                .header("Authorization", "Bearer " + testJWTToken)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ProductResponseDto>>() {});

        List<ProductResponseDto> products = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(products).isNotNull();
        assertThat(products).isInstanceOf(List.class);

        assertThat(products.get(0).getId()).isEqualTo(1);
        assertThat(products.get(0).getName()).isEqualTo("초코송이");
        assertThat(products.get(0).getPrice()).isEqualTo(1000);
    }

    @Test
    @Order(11)
    void 위시_리스트_상품_추가_정상_테스트(){
        System.out.println("Add Product to WishList success test");
        var url = "http://localhost:" + port + "/api/members/wishlist";
        WishListProductRequestDto wishListProductRequestDto = new WishListProductRequestDto(2L);
        var response = client.post()
                .uri(url)
                .header("Authorization", "Bearer " + testJWTToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(wishListProductRequestDto)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ProductResponseDto>>() {});

        List<ProductResponseDto> products = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(products).isNotNull();
        assertThat(products).isInstanceOf(List.class);

        assertThat(products.get(1).getId()).isEqualTo(2);
        assertThat(products.get(1).getName()).isEqualTo("포스틱");
        assertThat(products.get(1).getPrice()).isEqualTo(1500);
    }

    @Test
    @Order(12)
    void 위시_리스트_상품_삭제_정상_테스트(){
        System.out.println("Delete Product to WishList success test");
        var url = "http://localhost:" + port + "/api/members/wishlist/" + 1;
        var response = client.delete()
                .uri(url)
                .header("Authorization", "Bearer " + testJWTToken)
                .retrieve()
                .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () -> client.delete()
                                .uri(url)
                                .header("Authorization", "Bearer " + testJWTToken)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    @Order(13)
    void 위시_리스트_없는_상품_삭제_NOT_FOUND_테스트(){
        System.out.println("Delete Product to WishList NOT FOUND test");
        var url = "http://localhost:" + port + "/api/members/wishlist/" + 1;
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () -> client.delete()
                                .uri(url)
                                .header("Authorization", "Bearer " + testJWTToken)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }
}

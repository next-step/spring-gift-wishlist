package gift.controller;

import gift.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(statements = "delete from wish")
@Sql(statements = "delete from member")
@Sql(statements = "delete from product")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WishRestControllerTest {

    @LocalServerPort
    private int port;
    private RestClient client = RestClient.create();

    @Test
    @DisplayName("로그인 멤버 상품 조회")
    void getProducts() {
        String url = "http://localhost:" + port + "/api/members/register";
        ResponseEntity<CreateMemberResponse> registerResponse = client.post()
                .uri(url)
                .body(new CreateMemberRequest("test@exam.com", "1234"))
                .retrieve()
                .toEntity(CreateMemberResponse.class);

        url = "http://localhost:" + port + "/api/members/login";
        ResponseEntity<LoginMemberResponse> loginResponse = client.post()
                .uri(url)
                .body(new LoginMemberRequest("test@exam.com", "1234"))
                .retrieve()
                .toEntity(LoginMemberResponse.class);

        String body = loginResponse.getBody().toString();
        int start = body.indexOf("token=") + "token=".length();
        int end = body.indexOf("]", start);
        String token = body.substring(start, end);

        url = "http://localhost:" + port + "/api/wishes/products";
        ResponseEntity<Void> response = client.get()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("위시 리스트에 상품 추가")
    void 위시리스트_상품_추가() {
        //상품 추가
        String url = "http://localhost:" + port + "/api/products";
        ResponseEntity<CreateProductResponse> productResponse = client.post()
                .uri(url)
                .body(new CreateProductRequest("product1", 1000, "exam.url"))
                .retrieve()
                .toEntity(CreateProductResponse.class);
        assertThat(productResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        url = "http://localhost:" + port + "/api/members/register";

        // 회원가입 및 로그인
        ResponseEntity<CreateMemberResponse> registerResponse = client.post()
                .uri(url)
                .body(new CreateMemberRequest("test@exam.com", "1234"))
                .retrieve()
                .toEntity(CreateMemberResponse.class);

        url = "http://localhost:" + port + "/api/members/login";
        ResponseEntity<LoginMemberResponse> loginResponse = client.post()
                .uri(url)
                .body(new LoginMemberRequest("test@exam.com", "1234"))
                .retrieve()
                .toEntity(LoginMemberResponse.class);

        String body = loginResponse.getBody().toString();
        int start = body.indexOf("token=") + "token=".length();
        int end = body.indexOf("]", start);
        String token = body.substring(start, end);


        //위시 리스트에 상품 추가
        url = "http://localhost:" + port + "/api/wishes";
        ResponseEntity<Void> response = client.post()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .body(new CreateWishRequest(registerResponse.getBody().id(), productResponse.getBody().id(), 1))
                .retrieve()
                .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }
}
package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import gift.dto.MemberRequestDto;
import gift.dto.wish.WishRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.client.HttpClientProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class WishListControllerTest {
    @LocalServerPort
    private int port;

    private RestClient restClient = RestClient.builder().build();

    @Test
    void 회원_가입_성공시_토큰이_반환() {
        var url = "http://localhost:" + port + "/api/members/register";

        MemberRequestDto member1 = new MemberRequestDto("abc123@gmail.com", "password");

        var response = restClient.post()
                .uri(url)
                .body(member1)
                .retrieve()
                .toEntity(String.class);

        System.out.println("createdToken = " + response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 장바구니에_상품을_추가하는_기능() {
        var url = "http://localhost:" + port + "/api/wishList";

        WishRequestDto wishRequestDto = new WishRequestDto(1L, 5);

        var response = restClient.post()
                .uri(url)
                .header("Authorization", "Bearer eyJhbGciOiJIUzM4NCJ9.eyJlbWFpbCI6ImFiYzEyM0BnbWFpbC5jb20iLCJtZW1iZXJJZCI6NH0.Dp-5yh3qQVTn7oLGwMnaVI4dLV5h9M5UG8aZBvMPXn0CWKu9U-XClswgiwy5xTCT")
                .body(wishRequestDto)
                .retrieve()
                .toEntity(String.class);
        System.out.println(response.getBody().toString());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }







}

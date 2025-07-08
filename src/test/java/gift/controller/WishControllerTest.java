package gift.controller;

import gift.dto.AuthResponseDto;
import gift.dto.MemberRequestDto;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WishControllerTest {
    private final RestClient restClient = RestClient.builder().build();

    @LocalServerPort
    private int port;
    private String baseUrl;

    @BeforeEach
    void setBaseUrl() {
        baseUrl = "http://localhost:"+ port;
    }

    @Test
    void Wish_추가_수정_get_list_테스트() {

        // Member 추가
        ResponseEntity<AuthResponseDto> registerResponseEntity = registerTestMember("demo@demo", "asdf");

        assertThat(registerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String token = registerResponseEntity.getBody().token();

        // Wish 추가
        ResponseEntity<String> wishCreatedResponseEntity = restClient.post()
                .uri(baseUrl + "/api/wishes/1")
                .header("Authorization", "Bearer " + token)
                .body(
                        new WishRequestDto(123)
                )
                .retrieve()
                .toEntity(String.class);

        assertThat(wishCreatedResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // get wish list
        ResponseEntity<List<WishResponseDto>> wishListResponseEntity = getWishList(token);

        assertThat(wishListResponseEntity.getBody()).isNotNull();
        assertThat(wishListResponseEntity.getBody().size()).isEqualTo(1);
        assertThat(wishListResponseEntity.getBody().get(0).count()).isEqualTo(123);
        assertThat(wishListResponseEntity.getBody().get(0).productResponseDto().id()).isEqualTo(1);

        // patch wish
        ResponseEntity<String> wishUpdatedResponseEntity = restClient.patch()
                .uri(baseUrl + "/api/wishes/1")
                .header("Authorization", "Bearer " + token)
                .body(
                        new WishRequestDto(321)
                )
                .retrieve()
                .toEntity(String.class);

        assertThat(wishUpdatedResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);


        // get wish list
        ResponseEntity<List<WishResponseDto>> wishListResponseEntity2 = getWishList(token);

        assertThat(wishListResponseEntity2.getBody()).isNotNull();
        assertThat(wishListResponseEntity2.getBody().size()).isEqualTo(1);
        assertThat(wishListResponseEntity2.getBody().get(0).count()).isEqualTo(321);
        assertThat(wishListResponseEntity2.getBody().get(0).productResponseDto().id()).isEqualTo(1);

    }

    private ResponseEntity<AuthResponseDto> registerTestMember(String email, String password) {
        return restClient.post()
                .uri(baseUrl + "/api/members/register")
                .body(new MemberRequestDto(email, password))
                .retrieve()
                .toEntity(AuthResponseDto.class);
    }

    private ResponseEntity<List<WishResponseDto>> getWishList(String token) {
        return restClient.get()
                .uri(baseUrl + "/api/wishes")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<WishResponseDto>>() {});
    }
}

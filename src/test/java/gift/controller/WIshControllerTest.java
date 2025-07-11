package gift.controller;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.dto.CreateMemberRequestDto;
import gift.dto.CreateProductRequestDto;
import gift.dto.CreateWishRequestDto;
import gift.dto.DeleteMemberRequestDto;
import gift.dto.DeleteWishRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.UpdateWishQuantityRequstDto;
import gift.dto.WishResponseDto;
import gift.entity.Product;
import gift.service.MemberService;
import gift.service.ProductService;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WIshControllerTest {
    @LocalServerPort
    private int port;
    String token;
    private RestClient client = RestClient.builder().build();

    @Autowired
    private MemberService memberService;

    @Test
    void 위시조회시_로그인_하지_않은_사용자는_401이_반환된다() {
        String url = "http://localhost:" + port + "/api/wishes";
        assertThatExceptionOfType(HttpClientErrorException.Unauthorized.class)
                .isThrownBy(() ->
                        client.get()
                                .uri(url)
                                .retrieve()
                                .toBodilessEntity()
                );
    }

    @Test
    void 위시등록에_성공하면_201가_반환된다() {
        String url = "http://localhost:" + port + "/api/wishes";
        CreateWishRequestDto requestDto = new CreateWishRequestDto(1L, 3L);
        ResponseEntity<WishResponseDto> response = client.post()
                .uri(url)
                .header("Authorization", token)
                .body(requestDto)
                .retrieve()
                .toEntity(WishResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 위시조회에_성공하면_200가_반환된다() {
        String url = "http://localhost:" + port + "/api/wishes";
        ResponseEntity<List<WishResponseDto>> response = client.get()
                .uri(url)
                .header("Authorization", token)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<WishResponseDto>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 없는_위시의_수량변경하면_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/wishes";
        UpdateWishQuantityRequstDto requestDto = new UpdateWishQuantityRequstDto(1L, 10L);
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.patch()
                                .uri(url)
                                .header("Authorization", token)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(WishResponseDto.class)
                );
    }

    @Test
    void 위시수정시_로그인_하지_않은_사용자는_401이_반환된다() {
        String url = "http://localhost:" + port + "/api/wishes";
        UpdateWishQuantityRequstDto requestDto = new UpdateWishQuantityRequstDto(1L, 10L);

        assertThatExceptionOfType(HttpClientErrorException.Unauthorized.class)
                .isThrownBy(() ->
                        client.patch()
                                .uri(url)
                                .body(requestDto)
                                .retrieve()
                                .toBodilessEntity()
                );
    }


    @Test
    void 등록한_위시의_수량변경에_성공하면_200가_반환된다() {

        String url = "http://localhost:" + port + "/api/wishes";
        CreateWishRequestDto requestDto = new CreateWishRequestDto(1L, 3L);
        client.post()
                .uri(url)
                .header("Authorization", token)
                .body(requestDto)
                .retrieve()
                .toEntity(WishResponseDto.class);

        UpdateWishQuantityRequstDto updateRequestDto = new UpdateWishQuantityRequstDto(1L, 10L);
        ResponseEntity<WishResponseDto> response = client.patch()
                .uri(url)
                .header("Authorization", token)
                .body(updateRequestDto)
                .retrieve()
                .toEntity(WishResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 없는_위시를_삭제하면_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/wishes";
        DeleteWishRequestDto requestDto = new DeleteWishRequestDto(1L);
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.method(HttpMethod.DELETE)
                                .uri(url)
                                .header("Authorization", token)
                                .body(requestDto)
                                .retrieve()
                                .toBodilessEntity()
                );
    }

    @Test
    void 등록한_위시의_삭제에_성공하면_204가_반환된다() {

        String url = "http://localhost:" + port + "/api/wishes";
        CreateWishRequestDto requestDto = new CreateWishRequestDto(1L, 3L);
        client.post()
                .uri(url)
                .header("Authorization", token)
                .body(requestDto)
                .retrieve()
                .toEntity(WishResponseDto.class);

        DeleteWishRequestDto deleteWishRequestDto = new DeleteWishRequestDto(1L);
        ResponseEntity<Void> response = client.method(HttpMethod.DELETE)
                .uri(url)
                .header("Authorization", token)
                .body(deleteWishRequestDto)
                .retrieve()
                .toBodilessEntity();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


    @AfterEach
    void deleteTestUser() {
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

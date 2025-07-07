package gift.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.dto.CreateMemberRequestDto;
import gift.dto.JWTResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {

    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();

    @Test
    void 회원가입하면_201이_반환된다() {
        String url = "http://localhost:" + port + "/api/members/register";
        CreateMemberRequestDto memberRequestDto = new CreateMemberRequestDto("testUser2@asdasd.asd", "asd");
        ResponseEntity<JWTResponseDto> response = client.post()
                .uri(url)
                .body(memberRequestDto)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<JWTResponseDto>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 로그인하면_200이_반환된다() {

        String url = "http://localhost:" + port + "/api/members/register";
        CreateMemberRequestDto memberRequestDto = new CreateMemberRequestDto("testUser2@asdasd.asd", "asd");
        client.post()
                .uri(url)
                .body(memberRequestDto)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<JWTResponseDto>() {
                });


        url = "http://localhost:" + port + "/api/members/login";
        memberRequestDto = new CreateMemberRequestDto("testUser2@asdasd.asd", "asd");
        ResponseEntity<JWTResponseDto> response = client.post()
                .uri(url)
                .body(memberRequestDto)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 회원가입하지_않은_사용자는_404가_반환된다() {
        String url = "http://localhost:" + port + "/api/members/login";
        CreateMemberRequestDto memberRequestDto = new CreateMemberRequestDto("testUser2@asdasd.asd", "asd");
        ResponseEntity<JWTResponseDto> response = client.post()
                .uri(url)
                .body(memberRequestDto)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}

package gift.controller;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.dto.CreateMemberRequestDto;
import gift.dto.DeleteMemberRequestDto;
import gift.dto.JWTResponseDto;
import gift.exception.CustomErrorResponse;
import gift.exception.CustomException;
import gift.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MemberControllerTest {

    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();

    @Autowired
    private MemberService memberService;

    @Test
    void 회원가입하면_201이_반환된다() {
        String url = "http://localhost:" + port + "/api/members/register";
        CreateMemberRequestDto memberRequestDto = new CreateMemberRequestDto("testUser2@asdasd.asd",
                "asd");
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
        CreateMemberRequestDto memberRequestDto = new CreateMemberRequestDto("testUser2@asdasd.asd",
                "asd");
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
        CreateMemberRequestDto requestDto = new CreateMemberRequestDto("testUser2@asdasd.asd",
                "asd");
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(() ->
                        client.post()
                                .uri(url)
                                .body(requestDto)
                                .retrieve()
                                .toBodilessEntity()
                );
    }

    @AfterEach
    void deleteTestMembers() {
        DeleteMemberRequestDto requestDto = new DeleteMemberRequestDto("testUser2@asdasd.asd",
                "asd");
        try {
            memberService.deleteMember(requestDto);
        } catch (Exception e) {

        }
    }
}

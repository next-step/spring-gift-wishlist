package gift;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {
    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();

    @Test
    void 회원가입_중복_이메일_테스트(){
        System.out.println("Elready Exist Email Register test");
        MemberRequestDto requestDto = new MemberRequestDto("abc@pusan.ac.kr", "1234");
        var url = "http://localhost:" + port + "/api/members/register";
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
    void 회원가입_정상_테스트(){
        System.out.println("Member Register Success test");
        MemberRequestDto requestDto = new MemberRequestDto("abcd@pusan.ac.kr", "1234");
        var url = "http://localhost:" + port + "/api/members/register";
        var response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDto)
                .retrieve()
                .toEntity(MemberResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 로그인_없는_이메일_시도_테스트(){
        System.out.println("Member Login Not register Email test");
        MemberRequestDto requestDto = new MemberRequestDto("abcde@pusan.ac.kr", "1234");
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
    void 로그인_틀린_비밀번호_테스트(){
        System.out.println("Member Login Invalid password test");
        MemberRequestDto requestDto = new MemberRequestDto("abc@pusan.ac.kr", "12345");
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
    void 로그인_정상_테스트(){
        System.out.println("Member Login success test");
        MemberRequestDto requestDto = new MemberRequestDto("abc@pusan.ac.kr", "1234");
        var url = "http://localhost:" + port + "/api/members/login";
        var response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDto)
                .retrieve()
                .toEntity(MemberResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}

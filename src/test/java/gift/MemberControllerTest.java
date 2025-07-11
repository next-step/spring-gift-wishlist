package gift;

import gift.dto.MemberRequestDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.TokenResponseDto;
import gift.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {

    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();

    @Autowired
    private MemberRepository memberRepository;


    @BeforeEach
    void setUp() {
        memberRepository.deleteAllMembers();
        memberRepository.saveMember("tjdrj530@naver.com","tjdrj530","USER");
    }


    @Test
    void 회원가입시_정상입력되면_200이_반환된다(){
        String url = "http://localhost:" + port + "/api/members/register";
        MemberRequestDto memberRequestDto = new MemberRequestDto();
        memberRequestDto.setEmail("tjdrj530@naver.com");
        memberRequestDto.setPassword("tjdrj530");

        var response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberRequestDto)
                .retrieve()
                .toEntity(TokenResponseDto.class);
        assert(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    void 회원가입시_이메일을_제대로입력하지않으면_400이_반환된다(){
        String url = "http://localhost:" + port + "/api/members/register";
        MemberRequestDto memberRequestDto = new MemberRequestDto();
        memberRequestDto.setEmail("tjdrj530");
        memberRequestDto.setPassword("password");

        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(
                        () -> client.post()
                                .uri(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(memberRequestDto)
                                .retrieve()
                                .toEntity(TokenResponseDto.class)

                );
    }

    @Test
    void 정상로그인시_200이_반환된다(){
        String url = "http://localhost:" + port + "/api/members/login";
        MemberRequestDto memberRequestDto = new MemberRequestDto();
        memberRequestDto.setEmail("tjdrj530@naver.com");
        memberRequestDto.setPassword("tjdrj530");

        var response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberRequestDto)
                .retrieve()
                .toEntity(TokenResponseDto.class);
        assert(response.getStatusCode() == HttpStatus.OK);
    }


    @Test
    void 잘못된로그인시_400이_반환된다(){
        String url = "http://localhost:" + port + "/api/members/login";
        MemberRequestDto memberRequestDto = new MemberRequestDto();
        memberRequestDto.setEmail("tjdrj530@naver.com");
        memberRequestDto.setPassword("password");

        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(
                        () -> client.post()
                                .uri(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(memberRequestDto)
                                .retrieve()
                                .toEntity(TokenResponseDto.class)

                );
    }


}

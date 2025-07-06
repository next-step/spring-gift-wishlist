package gift;

import gift.dto.MemberLoginRequestDto;
import gift.dto.MemberLoginResponseDto;
import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberE2ETest {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void 회원가입() {
        //given
        final String name = "홍길동";
        final String email = "honggildong@email.com";
        final String password = "password";
        MemberRequestDto request = new MemberRequestDto(null, name, email, password);

        //when
        MemberResponseDto response = restClient.post()
                .uri("/api/members/register")
                .body(request)
                .retrieve()
                .body(MemberResponseDto.class);

        //then
        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo(name);
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.password()).isEqualTo(password);
    }

    @Test
    void 로그인_성공시_JWT_토큰_반환() {
        // given
        final String name = "홍길동";
        final String email = "honggildong@email.com";
        final String password = "password";

        MemberRequestDto joinRequest = new MemberRequestDto(null, name, email, password);

        MemberResponseDto joinResponse = restClient.post()
                .uri("/api/members/register")
                .body(joinRequest)
                .retrieve()
                .body(MemberResponseDto.class);

        assertThat(joinResponse.id()).isNotNull();

        // when
        MemberLoginRequestDto loginRequest = new MemberLoginRequestDto(email, password);

        MemberLoginResponseDto loginResponse = restClient.post()
                .uri("/api/members/login")
                .body(loginRequest)
                .retrieve()
                .body(MemberLoginResponseDto.class);

        // then
        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.token()).isNotBlank();
    }

}

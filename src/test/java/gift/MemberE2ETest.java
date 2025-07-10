package gift;

import gift.dto.MemberLoginRequestDto;
import gift.dto.MemberLoginResponseDto;
import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
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
        final String email = "hong" + System.currentTimeMillis() + "@email.com"; // 중복 방지
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

    @Test
    void 내정보_조회() {
        // given
        String name = "홍길동";
        String email = "hong1@email.com";
        String password = "password";
        String token = 회원가입_후_토큰_발급(name, email, password);

        // when
        MemberResponseDto myInfo = restClient.get()
                .uri("/api/members/myInfo")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(MemberResponseDto.class);

        // then
        assertThat(myInfo).isNotNull();
        assertThat(myInfo.name()).isEqualTo(name);
        assertThat(myInfo.email()).isEqualTo(email);
    }

    @Test
    void 내정보_수정() {
        // given
        String originalName = "홍길동";
        String originalEmail = "hong2@email.com";
        String password = "password";
        String token = 회원가입_후_토큰_발급(originalName, originalEmail, password);

        MemberRequestDto updateRequest = new MemberRequestDto(null, "이순신", "lee@email.com", "new-password");

        // when
        MemberResponseDto updatedInfo = restClient.put()
                .uri("/api/members/myInfo")
                .header("Authorization", "Bearer " + token)
                .body(updateRequest)
                .retrieve()
                .body(MemberResponseDto.class);

        // then
        assertThat(updatedInfo.name()).isEqualTo("이순신");
        assertThat(updatedInfo.email()).isEqualTo("lee@email.com");
    }

    @Test
    void 내정보_삭제() {
        // given
        String name = "홍길동";
        String email = "hong3@email.com";
        String password = "password";
        String token = 회원가입_후_토큰_발급(name, email, password);

        // when
        var deleteResponse = restClient.delete()
                .uri("/api/members/withdraw")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toBodilessEntity();

        // then
        assertThat(deleteResponse.getStatusCode().is2xxSuccessful()).isTrue();
    }

    private String 회원가입_후_토큰_발급(String name, String email, String password) {
        // 회원가입
        MemberRequestDto joinRequest = new MemberRequestDto(null, name, email, password);

        restClient.post()
                .uri("/api/members/register")
                .body(joinRequest)
                .retrieve()
                .body(MemberResponseDto.class);

        // 로그인
        MemberLoginRequestDto loginRequest = new MemberLoginRequestDto(email, password);
        MemberLoginResponseDto loginResponse = restClient.post()
                .uri("/api/members/login")
                .body(loginRequest)
                .retrieve()
                .body(MemberLoginResponseDto.class);

        return loginResponse.token();
    }

}
